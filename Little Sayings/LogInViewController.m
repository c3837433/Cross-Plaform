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
    [self.view setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"imagesbg"]]];
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
                                                // make sure email is validated
                                                [self checkEmailVerified:user];
                                                
                                            } else {
                                                if (error.code == 101) {
                                                    // Invalid Login credentials
                                                    [self alertUserWithTitle:@"Email or Password are Incorrect" message:@"Please try again."];
                                                } else {
                                                    // see what happened
                                                    NSLog(@"Error: %@", error.description);
                                                    [self alertUserWithTitle:@"Problem Logging In" message:@"Please try again."];
                                                }
                                            }
                                        }];
    }
}

-(void)alertUserWithTitle:(NSString*)title message:(NSString*)message {
    // Display alert to user
    [[[UIAlertView alloc] initWithTitle:title message:message delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
}

-(void)checkEmailVerified: (PFUser*) thisUser {
    // for WEEK 2, assume email is verified
    [self dismissViewControllerAnimated:true completion:nil];
    /* WEEK 4 EMAIL VERIFICATION
     // see if this user verified their email
    BOOL verified = [thisUser objectForKey:@"emailVerified"];
    if (!verified) {
        // alert user
        [self alertUserWithTitle:@"Email not Verified" message:@"Please verify your email and try again."];
        // Log user out of Parse
        [PFUser logOut];
    } else {
        // Move to main screen
        [self dismissViewControllerAnimated:true completion:nil];
    }
     */
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
