//
//  SayingListViewController.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import "SayingListViewController.h"
#import <Parse/Parse.h>
#import "SayingCell.h"

@implementation SayingListViewController


#pragma mark - PARSE PFQUERY TABLEVIEW METHODS

// Init for storyboards
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

// Get any Saying stories
- (PFQuery *)queryForTable {
    // Get all stories available for this current user
    PFQuery *query = [PFQuery queryWithClassName:self.parseClassName];
    [query setLimit:0];
    [query orderByDescending:@"createdAt"];
    return query;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma TABLEVIEW METHODS
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath object:(PFObject *)object
{
    // Create a testing cell
    SayingCell *cell = [tableView dequeueReusableCellWithIdentifier:@"sayingCell"];
    //SayingCell* cell = [tableView dequeueReusableCellWithIdentifier:@"sayingCell" forIndexPath:indexPath];
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
        [thisSaying deleteInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            if (succeeded) {
                // reload objects
                [self loadObjects];
            }
        }];
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

- (void)viewDetailsView:(DetailViewController *)controller didUpdateSaying:(BOOL)update
{
     NSLog(@"Returned from details");
    if (update) {
        NSLog(@"User updated item, refreshing list");
        [self loadObjects];
    }
}

- (IBAction)logOutUser:(id)sender {
    // Log user out of Parse
    [PFUser logOut];
    // Return to launch
    [self dismissViewControllerAnimated:NO completion:nil];
}


-(void) viewWillAppear:(BOOL)animated {
    // Set alll navigation text to white
   [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];
}

#pragma mark - Segue Methods
-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(UIButton*)sender
{
    if ([segue.identifier isEqualToString:@"segueToDetail"])
    {
        // Get this saying
        NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        
        PFObject* saying = [self.objects objectAtIndex:indexPath.row];
        DetailViewController* detailVC = segue.destinationViewController;
        detailVC.delegate = self;
        detailVC.thisSaying = saying;
    }
}




@end
