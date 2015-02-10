//
//  DetailViewController.h
//  Little Sayings
//
//  Created by Angela Smith on 2/9/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@interface DetailViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate>

@property(nonatomic, strong) IBOutlet UIView* detailView;
@property (nonatomic, strong) IBOutlet UIView* editView;

@property (nonatomic, strong) IBOutlet UITextView* detailTextView;
@property (nonatomic, strong) IBOutlet UITextField* editNameField;
@property (nonatomic, strong) IBOutlet UITextField* editAgeField;
@property (nonatomic, strong) IBOutlet UITextField* editDateField;
@property (nonatomic, strong) IBOutlet UITextView* editSayingView;

@property (nonatomic, strong) PFObject* thisSaying;


#pragma mark SAYING CLASS KEYS
#define aChildName  @"Name"
#define aChildAge @"Age"
#define aChildSaying @"ChildSaying"
#define aChildSayingDate @"SayingDate"
#define aSayingClass @"Saying"



@end
