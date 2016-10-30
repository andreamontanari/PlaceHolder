//
//  ViewController.swift
//  Placeholder
//
//  Created by Andrea Montanari on 09/08/16.
//  Copyright © 2016 Andrea Montanari. All rights reserved.
//

import Foundation
import UIKit
import GoogleMaps

class ViewController: UIViewController {
    
    var locationManager: CLLocationManager?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func savePlacePressed(sender: UIButton) {
        
         performSegueWithIdentifier("mapsIdentifier", sender: self)
        /*
        if CLLocationManager.authorizationStatus() == .AuthorizedWhenInUse {
            performSegueWithIdentifier("mapsIdentifier", sender: self)
        }*/ /*else if CLLocationManager.authorizationStatus() == .Denied {
            let alert = UIAlertController(title: "Your GPS is turned OFF", message: "GPS access is restricted. In order to save a place, please enable GPS in the Settigs app under Privacy, Location Services.", preferredStyle: UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title: "Open Settings", style: UIAlertActionStyle.Default, handler: { (alert: UIAlertAction!) in
                UIApplication.sharedApplication().openURL(NSURL(string:UIApplicationOpenSettingsURLString)!)
            }))
            
            //UIApplicationOpenSettingsURLString
            
            presentViewController(alert, animated: true, completion: nil)
        } else {
            locationManager!.requestWhenInUseAuthorization()
        }*/
    }
    
}

