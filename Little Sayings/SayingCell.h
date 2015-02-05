//
//  SayingCell.h
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//

#import <ParseUI/ParseUI.h>

@interface SayingCell : PFTableViewCell


@property (nonatomic, strong) IBOutlet UILabel* childNameLabel;
@property (nonatomic, strong) IBOutlet UILabel* childAgeLabel;
@property (nonatomic, strong) IBOutlet UILabel* childSayingLabel;
@property (nonatomic, strong) IBOutlet UILabel* childsayingDateLabel;

@property (nonatomic, strong) IBOutlet PFObject* thisSaying;


-(void)setSaying:(PFObject*)saying;

#pragma mark SAYING CLASS KEYS
#define aChildName  @"Name"
#define aChildAge @"Age"
#define aChildSaying @"ChildSaying"
#define aChildSayingDate @"SayingDate"

@end
