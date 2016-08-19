//
//  Utils.swift
//  Placeholder
//
//  Created by Andrea Montanari on 19/08/16.
//  Copyright © 2016 Andrea Montanari. All rights reserved.
//

import Foundation
import UIKit

func showToast(title: String, message: String, vc: UIViewController) {

            let dialog = UIAlertController(title: title,
                               message: message,
                               preferredStyle: UIAlertControllerStyle.Alert)
            // Present the dialog
            vc.presentViewController(dialog,
                      animated: false,
                      completion: {
                        let delayTime = dispatch_time(DISPATCH_TIME_NOW, Int64(1 * Double(NSEC_PER_SEC)))
                        dispatch_after(delayTime, dispatch_get_main_queue()) {
                            dialog.dismissViewControllerAnimated(true, completion: nil)
                        }
                        
            })
}