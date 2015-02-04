//
//  SignUpViewController.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import "SignUpViewController.h"
#import <Parse/Parse.h>

@interface SignUpViewController ()

@end

@implementation SignUpViewController
@synthesize emailTextField, passwordTextField, confirmPasswordField;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)checkSignUpCredentials:(id)sender {
    // Get the values from the fields
    NSString* email = emailTextField.text;
    NSString* password = passwordTextField.text;
    NSString* passwordC = confirmPasswordField.text;
    // Make sure the passwords match
    if (![password isEqual:passwordC]) {
        // Alert user
        [self alertUserWithTitle:@"Passwords don't match" message:@"Please try again."];
    } else {
        // Make sure the the fields are not blandk
        if (([email isEqual:@""]) || ([password isEqual:@""])) {
            // Alert user we need both values
            [self alertUserWithTitle:@"Missing Info" message:@"Please make sure all fields are filled in."];
        } else {
            // Attempt to log user in
            PFUser* newUser = [PFUser user];
            newUser.username = email;
            newUser.password = password;
            [newUser signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
                if (succeeded) {
                    // return all the way back to the launch screen
                    [self.presentingViewController.presentingViewController dismissViewControllerAnimated:NO completion:NULL];
                } else if (error) {
                    // check if code 101
                    if (error.code == 101) {
                        // This user is already registered
                        [self alertUserWithTitle:@"Already Registered" message:@"This email has already been registeres."];
                    } else {
                         [self alertUserWithTitle:@"Unable to Register" message:@"Please try again later."];
                    }
                }
            }];
        }
    }
}

-(void)alertUserWithTitle:(NSString*)title message:(NSString*)message {
    // Display alert to user
    [[[UIAlertView alloc] initWithTitle:title message:message delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
}

- (IBAction)cancelSignUp:(id)sender {
    // clear out textviews
    emailTextField.text = @"";
    passwordTextField.text = @"";
    confirmPasswordField.text = @"";
    [self dismissViewControllerAnimated:true completion:nil];
}

#pragma mark UITEXTFIELD DELEGATE
// when user clicks off a textfield
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
        [confirmPasswordField becomeFirstResponder];
    }
    else if (textField == confirmPasswordField)
    {
        // Last one, close the keyboard
        [confirmPasswordField resignFirstResponder];
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
