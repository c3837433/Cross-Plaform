//
//  SayingCell.m
//  Little Sayings
//
//  Created by Angela Smith on 2/4/15.
//  Copyright (c) 2015 Angela Smith. All rights reserved.
//
#import <Parse/Parse.h>
#import "SayingCell.h"

@implementation SayingCell
@synthesize childAgeLabel, childNameLabel, childsayingDateLabel, childSayingLabel, thisSaying;

// Set up the cell
-(void)setSaying:(PFObject*)saying {
    thisSaying = saying;
    
    // see if we have values and set them
    // NAME
    if ([thisSaying objectForKey:aChildName]) {
        // set in text view
        childNameLabel.text = [thisSaying objectForKey:aChildName];
    }
    
    // AGE
    if ([thisSaying objectForKey:aChildAge]) {
        // set in text view
        NSString* ageString;
        int age = [[thisSaying objectForKey:aChildAge] intValue];
        if (age == 0) {
            ageString = @"Unknown";
        } else {
            ageString = [NSString stringWithFormat:@"Age %d", age];
        }
        
        childAgeLabel.text = ageString;
    }
    
    // SAYING
    if ([thisSaying objectForKey:aChildSaying]) {
        // set in text view
        childSayingLabel.text = [thisSaying objectForKey:aChildSaying];
    }
    // SAYING DATE
    if ([thisSaying objectForKey:aChildSayingDate]) {
        // set in text view
        NSDate* sayingDate = [thisSaying objectForKey:aChildSayingDate];
        // Create the date formatter
        NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
        // and the format
        [dateFormatter setDateFormat:@"MM/dd/yyyy"];
        // Create the string from the date
        NSString* dateString = [dateFormatter stringFromDate:sayingDate];
        // Set date in textview
        childsayingDateLabel.text = dateString;
    }

}



@end
