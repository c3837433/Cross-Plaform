//
//  SayingListViewController.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import "SayingListViewController.h"
#import "AddSayingViewController.h"
#import "Reachability.h"
#import <Parse/Parse.h>
#import <ParseUI/ParseUI.h>
#import "SayingCell.h"
#import "AppDelegate.h"

@implementation SayingListViewController
@synthesize syncCheckTimer, networkStatus;

#pragma mark - PARSE PFQUERY TABLEVIEW METHODS
// INIT FOR STORYBOARD
-(id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithClassName:@"Saying"];
    self = [super initWithCoder:aDecoder];
    if (self) {
        self.parseClassName = @"Saying";
        // enable pull to refresh
        self.pullToRefreshEnabled = YES;
        // set the first 15 available stories in the list
        self.objectsPerPage = 15;
    }
    return self;
}

//GET ALL SAYING STORIES FOR THIS USER
- (PFQuery *)queryForTable {
    // Get all stories available for this current user
    PFQuery *query = [PFQuery queryWithClassName:self.parseClassName];
    [query orderByDescending:@"createdAt"];
    [query setCachePolicy:kPFCachePolicyCacheThenNetwork];

    // If there is no network connection, we will hit the cache first.
    BOOL hasConnection = [[[UIApplication sharedApplication] delegate] performSelector:@selector(networkAvailable)];
    if (!hasConnection) {
        NSLog(@"No connection found, querying from cache");
        [query setCachePolicy:kPFCachePolicyCacheThenNetwork];
        syncBtn.enabled = NO;
    }
    return query;
}

// PREPARE FOR EMPTY TABLE VIEW
- (void)objectsDidLoad:(NSError *)error {

    [super objectsDidLoad:error];
    if (self.objects.count == 0) {
        NSLog(@"there are: %ld objects loaded", self.objects.count);
        // Display a message when the table is empty
        UIImageView* messageImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.view.bounds.size.width, self.view.bounds.size.height)];
        messageImage.image = [UIImage imageNamed:@"noSaying"];
        messageImage.contentMode = UIViewContentModeCenter;
        self.tableView.backgroundView = messageImage;
        self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    }
}

#pragma TABLEVIEW METHODS
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath object:(PFObject *)object
{
    // Get the saying cell
    SayingCell *cell = [tableView dequeueReusableCellWithIdentifier:@"sayingCell"];
    if (cell != nil)
    {
        // Set the saying to the cell
        PFObject* saying = self.objects[indexPath.row];
        [cell setSaying:saying];
    }
    return cell;
}

// DELETE ITEM IN TABLEVIEW
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        PFObject* thisSaying = [self.objects objectAtIndex:indexPath.row];
        if (syncBtn.enabled) {
            [thisSaying deleteInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
                if (succeeded) {
                    // reload objects
                    [self loadObjects];
                }
            }];
        } else {
            [self alertUserWithTitle:@"No Network" message:@"Try deleting when network resumes"];
        }
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    // See what cell we are in
    if (indexPath.row == self.objects.count) {
        // This is a cell for the next row of items
        return 40.0f;
    } else {
        // Get the current saying
        PFObject* sayingObject = [self.objects objectAtIndex:indexPath.row];
        // and the text of the saying
        NSString* sayingText = [sayingObject objectForKey:aChildSaying];
        // Define the label font
        UIFont* font = [UIFont fontWithName:@"AvenirNext-Regular" size:14];
        
        // Get the width of the label by taking the cell and subtracting the side margins
        CGFloat labelWidth = tableView.frame.size.width - 16;
        // Create an attributed string from the text so we can create the shape
        NSAttributedString *attributedText = [[NSAttributedString alloc] initWithString:sayingText attributes:@{NSFontAttributeName: font}];
        // Create a rect that would fit the text label
        CGRect labelRect = [attributedText boundingRectWithSize:(CGSize){labelWidth, CGFLOAT_MAX} options:NSStringDrawingUsesLineFragmentOrigin context:nil];
        // Get the size, then the height
        CGSize labelSize = labelRect.size;
        labelSize.height = ceilf(labelSize.height);
        // return the height needed for the label plus the base tableview cell height
        return labelSize.height + 60;
    }
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
}


#pragma DETAIL VIEW CONTROLLER DELEGATE
- (void)viewDetailsView:(DetailViewController *)controller didUpdateSaying:(BOOL)update
{
     NSLog(@"Returned from details");
    if (update) {
        NSLog(@"User updated item, refreshing list");
        [self loadObjects];
    }
}

#pragma LOG OUT USER
- (IBAction)logOutUser:(id)sender {
    // Log user out of Parse
    [PFUser logOut];
    // stop timer
    [self stopTimer];
    // stop receiving notifications
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kReachabilityChangedNotification object:nil];
    // Return to launch
    [self dismissViewControllerAnimated:NO completion:nil];
}


#pragma mark - Segue Methods
-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(UIButton*)sender
{
    // stop timer
    [self stopTimer];
    
    if ([segue.identifier isEqualToString:@"segueToDetail"])
    {
        // Get this saying
        NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        
        PFObject* saying = [self.objects objectAtIndex:indexPath.row];
        DetailViewController* detailVC = segue.destinationViewController;
        detailVC.delegate = self;
        detailVC.thisSaying = saying;
    }  else if ([segue.identifier isEqualToString:@"segueToAdd"]) {
        // set the controller so we can handle the completion block
        AddSayingViewController* addSayingVC = segue.destinationViewController;
        [addSayingVC setSayingListController:self];
    }

}


#pragma VIEW CONTROLLER METHODS
-(void) viewWillAppear:(BOOL)animated {
    // Set alll navigation text to white
    [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];
    // start timer
    [self startSyncTimer];
}

- (void)viewDidLoad
{
    syncBtn = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemRefresh target:self action:@selector(syncDataWithParse)];
    
    // create the two buttons
    self.navigationItem.rightBarButtonItems = [NSArray arrayWithObjects:syncBtn, self.addBtn, nil];
    // register the reciever
    // set up the notificaiton
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(reachabilityChanged:)
                                                 name:kReachabilityChangedNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(startSyncTimer)
                                                 name:UIApplicationWillEnterForegroundNotification object:nil];
    
    [super viewDidLoad];
}



- (void) reachabilityChanged:(NSNotification *)note {
    // get the current status
    Reachability* curReach = [note object];
    networkStatus = [curReach currentReachabilityStatus];
    switch (networkStatus) {
        case NotReachable:        {
            NSLog(@"Parse not reachable");
            self.isServerReachable = false;
            syncBtn.enabled = NO;
            [self alertUserWithTitle:@"Lost Network Connection" message:nil];
            // stop timer
            [self stopTimer];
            break;
        }
        case ReachableViaWWAN:        {
            NSLog(@"Accessing Parse via wwan");
            // start timer
            self.isServerReachable = true;
            [self startSyncTimer];
            syncBtn.enabled = YES;
            break;
        }
        case ReachableViaWiFi:        {
            NSLog(@"Accessing Parse via wifi");
            // start timer
            [self startSyncTimer];
            self.isServerReachable = true;
            syncBtn.enabled = YES;
            break;
        }
    }
}
-(void) stopTimer {
    NSLog(@"Stopping Timer");
    [syncCheckTimer invalidate];
}
-(void)startSyncTimer {
    NSLog(@"Starting timer");
    syncCheckTimer = [NSTimer scheduledTimerWithTimeInterval:20.0f target:self selector:@selector(verifyConnection) userInfo:nil repeats:YES];
}

-(void)verifyConnection {
    NSLog(@"Checking connection");
    // Check if we have a connection
    if ([[UIApplication sharedApplication].delegate performSelector:@selector(networkAvailable)]) {
        // sync with parse
        NSLog(@"Loading data");
        [self loadObjects];
    }

}

-(void)syncDataWithParse {
    [self loadObjects];
}

-(void)alertUserWithTitle:(NSString*)title message:(NSString*)message {
    // Display alert to user
    [[[UIAlertView alloc] initWithTitle:title message:message delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
