//
//  DetailViewController.m
//  Little Sayings
//
//  Created by Angela Smith on 2/9/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import "DetailViewController.h"
#import <QuartzCore/QuartzCore.h> 


@interface DetailViewController ()

@end

@implementation DetailViewController
@synthesize editAgeField, editNameField, editDateField, editView, editSayingView, detailTextView, thisSaying, detailView, updateSayingButton;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Set up the buttons so the user can switch to edit mode for this item
    editSayingButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemEdit target:self action:@selector(updateEditOrDisplayViews:)];
    editSayingButton.tag = 1;
    // Since we will start in detail view, set the edit button at the top
    self.navigationItem.rightBarButtonItem = editSayingButton;
    // Define the cancel button for later
    cancelEditButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(updateEditOrDisplayViews:)];
    cancelEditButton.tag = 2;
    
    // And prepare the date picker to be displayed when the user pressses within the date field
    UIDatePicker* editDatePicker = [[UIDatePicker alloc] init];
    editDatePicker.datePickerMode = UIDatePickerModeDate;
    [editDateField setInputView:editDatePicker];
    // update on change
    [editDatePicker addTarget:self action:@selector(updateDateLabelWithNewDate:)forControlEvents:UIControlEventValueChanged];
    
    // set the bools to no
    changedAge = changedDate = changedName = changedSaying = NO;
    // set up story
    [self setUpStoryDetails];
    // set the default delegate update
    // set the value for update
    [self.delegate viewDetailsView:self didUpdateSaying:NO];
}

-(void)setUpStoryDetails {
    // set this story's information within the text view
    // DATE
    NSDate* sayingDate = [thisSaying objectForKey:aChildSayingDate];
    // Create the date formatter
    NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
    // and the format
    [dateFormatter setDateFormat:@"MM/dd/yyyy"];
    // Create the string from the date
    NSString* dateString = [dateFormatter stringFromDate:sayingDate];
    
    // AGE
    int age = [[thisSaying objectForKey:aChildAge] intValue];
    NSString* sayingDetails;
    if (age == 0) {
        sayingDetails = [NSString stringWithFormat:@"At an unknown age, %@ said,\n      \"%@ \n\n %@", [thisSaying objectForKey:aChildName], [thisSaying objectForKey:aChildSaying], dateString];
    } else {
        sayingDetails = [NSString stringWithFormat:@"At age %d, %@ said,\n      \"%@ \n\n %@", age, [thisSaying objectForKey:aChildName], [thisSaying objectForKey:aChildSaying], dateString];
    }
    
    detailTextView.text = sayingDetails;
    editDateField.text = dateString;
    editAgeField.text = [NSString stringWithFormat:@"%d", age];
    editNameField.text = [thisSaying objectForKey:aChildName];
    editSayingView.text = [thisSaying objectForKey:aChildSaying];
    
    // begin listening for change events
    [editNameField addTarget:self action:@selector(userChangedField:) forControlEvents:UIControlEventEditingChanged];
    [editAgeField addTarget:self action:@selector(userChangedField:) forControlEvents:UIControlEventEditingChanged];
    [editDateField addTarget:self action:@selector(userChangedField:) forControlEvents:UIControlEventEditingChanged];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void) userChangedField:(UITextField*)field {
    // Display the button to allow saying update
    updateSayingButton.hidden = NO;
    // see what was changed
    switch (field.tag) {
        case 1: // Name
            changedName = YES;
            NSLog(@"The user changed name field");
            break;
        case 2: // age
            changedAge = YES;
            NSLog(@"The user changed age field");
            break;
        default:
            break;
    }
}

-(void)textViewDidChange:(UITextView *)textView
{
    NSLog(@"The user updated the saying");
    changedSaying = YES;
    // Display the button to update
    updateSayingButton.hidden = NO;
    
}

-(void)switchViews {
    // Switch between the edit and detail views
    [editView setHidden:[editView isHidden]];
    [editView setHidden:![editView isHidden]];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark - Change Views
-(void)updateEditOrDisplayViews:(UIButton*)button
{
    if (button) {
        // see what the tag is
        if (button.tag == 2) {
            /// the user needs is canceling the update, reset the values and return to the detail view
            changedName = NO;
            changedAge = NO;
            changedDate = NO;
            changedSaying = NO;
            // hide the button
            updateSayingButton.hidden = YES;
            // Reset the views with the origional data
            [self setUpStoryDetails];
        }
    }
    // Display the opposite view
    [detailView setHidden:[editView isHidden]];
    [editView setHidden:![editView isHidden]];
    // Change the nav button
    self.navigationItem.rightBarButtonItem = ([editView isHidden]) ? editSayingButton : cancelEditButton;
}

#pragma UITEXTFIELD DELEGATE
// when user clicks off a field
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // Close the keyboard
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    // close the keynoard
    [textField resignFirstResponder];
    return NO;
}

#pragma DATE METHOD
-(void)updateDateLabelWithNewDate:(UIDatePicker*)datePicker {
    NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"MM/dd/yyyy"];
    //format the date from the picker and set it in the view
    NSString* stringFromDate = [dateFormatter stringFromDate:datePicker.date];
    editDateField.text = stringFromDate;
    // Set date update
    changedDate = YES;
    NSLog(@"The user changed date field");
    // Display the button to update
    updateSayingButton.hidden = NO;
}
-(IBAction)onUpdate:(UIButton*)button {
    // Get this class object
    PFQuery *query = [PFQuery queryWithClassName:aSayingClass];
    
    // and this saying object
    [query getObjectInBackgroundWithId:[thisSaying objectId] block:^(PFObject* saying, NSError *error) {
        // Only update what was changed
        if (changedName) {
            NSLog(@"Updating name");
            saying[aChildName] = editNameField.text;
        }
        if (changedAge) {
            NSLog(@"Updating age");
            // get the number
            NSInteger age = [editAgeField.text intValue];
            saying[aChildAge] = @(age);
        }
        if (changedDate) {
            NSLog(@"Updating date");
            // get the date
            NSString* enteredDate = editDateField.text;
            // Create the formatter
            NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
            // and the format
            [dateFormatter setDateFormat:@"MM/dd/yyyy"];
            NSDate* newDate = [dateFormatter dateFromString:enteredDate];
            saying[aChildSayingDate] = newDate;
        }
        if (changedSaying) {
            NSLog(@"Updating saying");
            saying[aChildSaying] = editSayingView.text;
        }
        [saying saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            // check for success
            if (succeeded) {
                // reset the booleans and hide the buttons
                [self resetChangeValuesForSaying:saying];
            }
        }];
        
    }];
}
-(void)resetChangeValuesForSaying:(PFObject*)updatedSaying {
    changedName = NO;
    changedAge = NO;
    changedDate = NO;
    changedSaying = NO;
    // hide the button
    updateSayingButton.hidden = YES;
    // change the view back
    [self updateEditOrDisplayViews:nil];
    // update the display text with the updated story
    thisSaying = updatedSaying;
    [self setUpStoryDetails];
    // set the value for update
    [self.delegate viewDetailsView:self didUpdateSaying:YES];
}



@end
