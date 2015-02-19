//
//  SayingListViewController.h
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ParseUI/ParseUI.h"
#import "DetailViewController.h"
#import "Reachability.h"

@interface SayingListViewController : PFQueryTableViewController <DetailViewControllerDelegate>

@property (nonatomic)  BOOL needToSync;
@property (nonatomic) BOOL isServerReachable;
@property (nonatomic, strong) IBOutlet UIBarButtonItem* addBtn;
@property (nonatomic, strong) NSTimer* syncCheckTimer;
@property (nonatomic) int networkStatus;


-(void) syncOnUpdate;
@end
