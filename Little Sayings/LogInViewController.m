//
//  LogInViewController.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import "LogInViewController.h"
#import <Parse/Parse.h>

@interface LogInViewController ()

@end

@implementation LogInViewController
@synthesize emailTextField, passwordTextField;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)checkLogInCredentials:(id)sender {
    // Get the values in the text fields
    NSString* email = emailTextField.text;
    NSString* password = passwordTextField.text;
    if (([email isEqual:@""]) || ([password isEqual:@""])) {
        // Alert user we need both values
        [self alertUserWithTitle:@"Missing Info" message:@"Please make sure both fields are filled in."];
    } else {
        // Attempt to log user in
        [PFUser logInWithUsernameInBackground:email password:password
                                        block:^(PFUser *user, NSError *error) {
                                            if (user) {
                                                // Move to main screen
                                                [self dismissViewControllerAnimated:true completion:nil];
                                            } else {
                                                // see what happened
                                                NSLog(@"Error: %@", error.description);
                                                [self alertUserWithTitle:@"Problem Logging In" message:@"Please try again."];
                                            }
                                        }];
    }
}

-(void)alertUserWithTitle:(NSString*)title message:(NSString*)message {
    // Display alert to user
    [[[UIAlertView alloc] initWithTitle:title message:message delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
}

#pragma mark UITEXTFIELD DELEGATE
// Close keyboard when moving off textfield
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // Close the keyboard
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    // Move to next field
    if(textField == emailTextField)
    {
        [passwordTextField becomeFirstResponder];
    } else if (textField == passwordTextField)
    {
        [passwordTextField resignFirstResponder];
    }
    return NO;
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
