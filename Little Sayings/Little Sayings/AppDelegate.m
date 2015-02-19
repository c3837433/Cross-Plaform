//
//  AppDelegate.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
// GITHUB LINK https://github.com/c3837433/Cross-Plaform

#import "AppDelegate.h"
#import <Parse/Parse.h>

@interface AppDelegate ()

@end

@implementation AppDelegate
@synthesize networkStatus;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    // [Optional] Power your app with Local Datastore. For more info, go to
    // https://parse.com/docs/ios_guide#localdatastore/iOS
    //[Parse enableLocalDatastore];
    
    // Initialize Parse.
    [Parse setApplicationId:@"N2wRnfGDdmtURsVA4IZfgXU5UdjREkHbQepbwxBv"
                  clientKey:@"kUzQRnQ1mpbyVJ8rLnjbfUYUnIHYqTotnjstXcxu"];
    
    // [Optional] Track statistics around application opens.
    [PFAnalytics trackAppOpenedWithLaunchOptions:launchOptions];
    
    UIBarButtonItem *barButtonAppearance = [UIBarButtonItem appearance];
    [barButtonAppearance setTintColor:[UIColor whiteColor]];
    
    // start monitering the network connection
    [self checkReachability];
    return YES;
}

- (void)checkReachability {
    // set up the notificaiton
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(reachabilityChanged:)
                                                 name:kReachabilityChangedNotification
                                               object:nil];
    // listen for internet access
    self.internetReachability = [Reachability reachabilityForInternetConnection];
    // get the current status
    networkStatus = self.internetReachability.currentReachabilityStatus;
    // start notifying the user
    [self.internetReachability startNotifier];
}

// update the status when it changes
- (void) reachabilityChanged:(NSNotification *)note {
    // get the current status
    Reachability* curReach = [note object];
    networkStatus = [curReach currentReachabilityStatus];
}

// access the current state within the other views
- (BOOL)networkAvailable {
    return networkStatus != NotReachable;
}
- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
