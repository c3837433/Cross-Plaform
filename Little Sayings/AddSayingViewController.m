//
//  AddSayingViewController.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import "AddSayingViewController.h"
#import <QuartzCore/QuartzCore.h> 
#import <Parse/Parse.h>

@interface AddSayingViewController ()

@end

@implementation AddSayingViewController
@synthesize childAgeView, childNameView, childSayingView, childSayingDateView;


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    // add a border to the textview
    [[childSayingView layer] setBorderColor:[[UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:204.0/255.0 alpha:0.5] CGColor]];
    [[childSayingView layer] setBorderWidth:1];
    [[childSayingView layer] setCornerRadius:8];
    
    // Create the date picker if user wants to change the date
    UIDatePicker* sayingDatePicker = [[UIDatePicker alloc] init];
    // set it to make dates
    sayingDatePicker.datePickerMode = UIDatePickerModeDate;
    // set it as the input type instead of a keyboard
    [childSayingDateView setInputView:sayingDatePicker];
    // and have it update the textfield with the selected date
    [sayingDatePicker addTarget:self action:@selector(updateDateLabelWithNewDate:)forControlEvents:UIControlEventValueChanged];
    
    // Update the textview with todays date
    childSayingDateView.text = [self getCurrentDate];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma UITEXTFIELD DELEGATE
// when user clicks off a textfield
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // Close the keyboard
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    // Move to next field
    if(textField == childNameView) {
        [childAgeView becomeFirstResponder];
    } else if (textField == childAgeView) {
        [childSayingView becomeFirstResponder];
    }
    
    return NO;
}

#pragma DATE METHODS
-(void)updateDateLabelWithNewDate:(UIDatePicker*)datePicker {
    NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"MM/dd/yyyy"];
    // get the date from the picker
    
    NSString* stringFromDate = [dateFormatter stringFromDate:datePicker.date];
    
    // set the selected text in the date picker
     childSayingDateView.text = stringFromDate;
}

-(NSString*) getCurrentDate {
    // create the formatter and format
    NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"MM/dd/yyyy"];
    // get today's date for the view
    return [dateFormatter stringFromDate:[NSDate date]];
}

-(IBAction)onSave:(UIButton*)sender {
    NSLog(@"Attempting to save saying");
    // Get the values of the saying
    NSString* saying = childSayingView.text;
    // Make sure we have a saying
    if (![saying isEqual:@""]) {
        // Gather the remaining
        // AGE
        NSInteger age = [childAgeView.text intValue];
        // NAME
        NSString* name = childNameView.text;
        
        // DATE
        NSString* dateString = childSayingDateView.text;
        // Create the formatter
        NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
        // and the format
        [dateFormatter setDateFormat:@"MM/dd/yyyy"];
        NSDate* sayingDate = [dateFormatter dateFromString:dateString];
        
        // Create a new Parse Object
        PFObject* newSaying = [PFObject objectWithClassName:aSayingClass];
        // Set values
        newSaying[aChildName] = name;
        newSaying[aChildAge] = @(age);
        newSaying[aChildSaying] = saying;
        newSaying[aChildSayingDate] = sayingDate;

        // set read/write permission to current user
        newSaying.ACL = [PFACL ACLWithUser:[PFUser currentUser]];
        // FInally, save this saying
        [newSaying saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            // See if we have any errors
            if (succeeded) {
                NSLog(@"Save successful!");
                // clear out cells, move user back to list, and update the list
                [self clearAndReturnUser];
            } else {
                if ([error code] == kPFErrorConnectionFailed) {
                    // Alert user we are unable to connect to parse
                    [self alertUserWithTitle:@"Unable to access server" message:@"Please try again later."];
                } else if (error) {
                    NSLog(@"Error: %@", [error userInfo][@"error"]);
                    [self alertUserWithTitle:@"Problem Saving" message:@"Please try again later"];
                }
            }
        }];
        
    } else {
        // alert the user we need a saying
        [self alertUserWithTitle:@"No Saying" message:@"Please enter a saying to save."];
    }

}

-(void)clearAndReturnUser{
    // Clear out the cells
    childNameView.text = @"";
    childAgeView.text = @"";
    childSayingView.text = @"";
    // reset the date
    childSayingDateView.text = [self getCurrentDate];
    // Return to list view
    [self dismissViewControllerAnimated:NO completion:nil];

}
-(void)alertUserWithTitle:(NSString*)title message:(NSString*)message {
    // Display alert to user
    [[[UIAlertView alloc] initWithTitle:title message:message delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
}

@end
