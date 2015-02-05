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

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath object:(PFObject *)object
{
    // Create a testing cell
    SayingCell *cell = [tableView dequeueReusableCellWithIdentifier:@"sayingCell"];
    if (cell != nil)
    {
        // Set the saying to the cell
        PFObject* saying = self.objects[indexPath.row];
        [cell setSaying:saying];
    }
    return cell;
}

- (IBAction)logOutUser:(id)sender {
    // Log user out of Parse
    [PFUser logOut];
    // Return to launch
    [self dismissViewControllerAnimated:NO completion:nil];
}

@end
