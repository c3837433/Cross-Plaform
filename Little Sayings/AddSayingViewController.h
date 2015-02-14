//
//  AddSayingViewController.h
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AddSayingViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate>

@property (nonatomic, strong) IBOutlet UITextField* childNameView;
@property (nonatomic, strong) IBOutlet UITextField* childAgeView;
@property (nonatomic, strong) IBOutlet UITextField* childSayingDateView;
@property (nonatomic, strong) IBOutlet UITextView* childSayingView;

-(void)updateDateLabelWithNewDate:(id)sender;

#pragma mark SAYING CLASS KEYS
#define aChildName  @"Name"
#define aChildAge @"Age"
#define aChildSaying @"ChildSaying"
#define aChildSayingDate @"SayingDate"
#define aSayingClass @"Saying"
#define aIsNew @"IsNew"

@end
