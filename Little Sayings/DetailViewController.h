//
//  DetailViewController.h
//  Little Sayings
//
//  Created by Angela Smith on 2/9/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@class DetailViewController;
@protocol DetailViewControllerDelegate <NSObject>
// define the delegate method when we update an object
- (void)viewDetailsView:(DetailViewController *)controller didUpdateSaying:(BOOL)update;
@end


@interface DetailViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate> {
    UIBarButtonItem* editSayingButton;
    UIBarButtonItem* cancelEditButton;
    BOOL changedName;
    BOOL changedDate;
    BOOL changedAge;
    BOOL changedSaying;
}


@property(nonatomic, strong) IBOutlet UIView* detailView;
@property (nonatomic, strong) IBOutlet UIView* editView;

@property (nonatomic, strong) IBOutlet UITextView* detailTextView;
@property (nonatomic, strong) IBOutlet UITextField* editNameField;
@property (nonatomic, strong) IBOutlet UITextField* editAgeField;
@property (nonatomic, strong) IBOutlet UITextField* editDateField;
@property (nonatomic, strong) IBOutlet UITextView* editSayingView;
@property (nonatomic, strong) IBOutlet UIButton* updateSayingButton;



@property (nonatomic, strong) PFObject* thisSaying;
// set up the delegate property
@property (nonatomic, weak) id <DetailViewControllerDelegate> delegate;


#pragma mark SAYING CLASS KEYS
#define aChildName  @"Name"
#define aChildAge @"Age"
#define aChildSaying @"ChildSaying"
#define aChildSayingDate @"SayingDate"
#define aSayingClass @"Saying"



@end
