//
//  AppDelegate.h
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
// GITHUB LINK https://github.com/c3837433/Cross-Plaform


#import <UIKit/UIKit.h>
#import "Reachability.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic) Reachability* internetReachability;
@property (nonatomic) int networkStatus;


- (BOOL)networkAvailable;
@end

