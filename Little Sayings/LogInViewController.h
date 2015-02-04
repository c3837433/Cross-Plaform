//
//  LogInViewController.h
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LogInViewController : UIViewController <UITextFieldDelegate>


@property (nonatomic, strong) IBOutlet UITextField* emailTextField;
@property (nonatomic, strong) IBOutlet UITextField* passwordTextField;

@end
