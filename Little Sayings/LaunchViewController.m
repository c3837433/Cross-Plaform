//
//  LaunchViewController.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import "LaunchViewController.h"
#import <Parse/Parse.h>
#import "LogInViewController.h"
#import "SayingListViewController.h"


@interface LaunchViewController ()

@end

@implementation LaunchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.view setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"imagesbg"]]];
    // Do any additional setup after loading the view.
}

#pragma CHECK FOR PARSE USER

-(void)viewDidAppear:(BOOL)animated {
    // Once views are ready, check to see if user is logged in, if they are view list otherwise lig in screen
    ![PFUser currentUser] ? ([self presentLogInView]) : ([self presentMainSayingView]);

}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma CHANGE VIEWS
-(void)presentLogInView {
     NSLog(@"User NOT logged in, view log in screen");
    // Get the log in vc
    LogInViewController* logInVC = [self.storyboard instantiateViewControllerWithIdentifier:@"logInVC"];
    // open it
    [self presentViewController:logInVC animated:NO completion:nil];

}

-(void) presentMainSayingView {
    // User is logged in
    NSLog(@"User is logged in, view main feed");
    // get the tableview nav controller
    UINavigationController* navController = [self.storyboard instantiateViewControllerWithIdentifier:@"sayingNavControl"];
    [self presentViewController:navController animated:NO completion:nil];
}

@end
