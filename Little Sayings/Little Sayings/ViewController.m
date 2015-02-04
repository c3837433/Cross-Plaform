//
//  ViewController.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import "ViewController.h"
#import <Parse/Parse.h>
@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)logOutUser:(id)sender {
    // Log user out of Parse
    [PFUser logOut];
    // Return to launch
    [self dismissViewControllerAnimated:NO completion:nil];
}

@end
