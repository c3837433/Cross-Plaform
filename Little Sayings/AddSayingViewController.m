//
//  AddSayingViewController.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import "AddSayingViewController.h"
#import <QuartzCore/QuartzCore.h> 

@interface AddSayingViewController ()

@end

@implementation AddSayingViewController
@synthesize childAgeView, childNameView, childSayingDateLabel, childSayingView;


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    // add a border to the textview
    [[childSayingView layer] setBorderColor:[[UIColor grayColor] CGColor]];
    [[childSayingView layer] setBorderWidth:2.3];
    [[childSayingView layer] setCornerRadius:15];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
