//
//  DetailViewController.swift
//  Placeholder
//
//  Created by Andrea Montanari on 19/08/16.
//  Copyright © 2016 Andrea Montanari. All rights reserved.
//

import Foundation


import Foundation
import UIKit
import RealmSwift
import MapKit
import Social

class DetailViewController: UIViewController {
    
    var currentPlace: Place!
    
    @IBOutlet weak var addressLbl: UILabel!
    @IBOutlet weak var coordsLbl: UILabel!
    @IBOutlet weak var commentLbl: UILabel!
    @IBOutlet weak var shareContainer: UIView!
    
    var defaultMessage: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        addressLbl.text = currentPlace.streetName
        coordsLbl.text = currentPlace.latlng
        let placeholder = NSLocalizedString("PLACE_COMMENT_PLACEHOLDER", comment: "")
        if currentPlace.placeComment == nil || currentPlace.placeComment == "" {
            commentLbl.text = "\"\(placeholder)\""
            defaultMessage = "\(currentPlace.streetName) \(currentPlace.latlng) #placeholder"
        } else {
            commentLbl.text = "\"\(currentPlace.placeComment!)\""
            defaultMessage = "\"\(currentPlace.placeComment!)\" - \(currentPlace.streetName) (\(currentPlace.latlng)) #placeholder"
        }
        
        
    }
    
    @IBAction func goBack(sender: UIBarButtonItem) {
        self.navigationController?.popViewControllerAnimated(true)
        //dismissViewControllerAnimated(true, completion: nil)
    }
    
    
    @IBAction func editCommentPressed(sender: UIButton) {
        
        //open dialog
        let alert = UIAlertController(title: NSLocalizedString("PLACE_COMMENT_TITLE", comment: ""), message: NSLocalizedString("PLACE_COMMENT_MSG", comment: ""), preferredStyle: UIAlertControllerStyle.Alert)
        
        alert.addAction(UIAlertAction(title: NSLocalizedString("CANCEL", comment: ""), style: UIAlertActionStyle.Cancel, handler:
            { (action: UIAlertAction!) in
                alert.dismissViewControllerAnimated(true, completion: nil)
        }))
        
        // configure text field
        alert.addTextFieldWithConfigurationHandler({(textField: UITextField) in
            textField.textColor = UIColor.blackColor()
            textField.placeholder = NSLocalizedString("PLACE_COMMENT_HINT", comment: "")
            textField.text = self.currentPlace.placeComment
        })
        
        alert.addAction(UIAlertAction(title: NSLocalizedString("SAVE", comment: ""), style: UIAlertActionStyle.Default, handler:{ (UIAlertAction)in
            //read it
            let textField = (alert.textFields?.first)! as UITextField
            //save it
            // Get the default Realm
            // You only need to do this once (per thread)
            let realm = try! Realm()
            
            // Add to the Realm inside a transaction
            try! realm.write {
                self.currentPlace.placeComment = textField.text
            }
            
            let placeholder = NSLocalizedString("PLACE_COMMENT_PLACEHOLDER", comment: "")
            
            if textField.text == nil || textField.text == ""  {
                self.commentLbl.text = "\"\(placeholder)\""
            } else {
                self.commentLbl.text = "\"\(textField.text!)\""
            }
            
            showToast(NSLocalizedString("PLACE_COMMENT_OK_TITLE", comment: ""), message: NSLocalizedString("PLACE_COMMENT_OK_MSG", comment: ""), vc: self)
            
        }))
        
        presentViewController(alert, animated: true, completion: nil)

        
        
    }
    

    @IBAction func deletePressed(sender: UIButton) {
        //open dialog
        let alert = UIAlertController(title: NSLocalizedString("DELETE_PLACE_TITLE", comment: ""), message: NSLocalizedString("DELETE_PLACE_MSG", comment: ""), preferredStyle: UIAlertControllerStyle.Alert)
        
        alert.addAction(UIAlertAction(title: NSLocalizedString("CANCEL", comment: ""), style: UIAlertActionStyle.Cancel, handler:
            { (action: UIAlertAction!) in
                alert.dismissViewControllerAnimated(true, completion: nil)
        }))
        
        alert.addAction(UIAlertAction(title: NSLocalizedString("DELETE_PLACE_CONFIRM", comment: ""), style: UIAlertActionStyle.Default, handler:{ (UIAlertAction)in
            // Get the default Realm
            // You only need to do this once (per thread)
            let realm = try! Realm()
            
            // Add to the Realm inside a transaction
            try! realm.write {
                realm.delete(self.currentPlace)
            }
            
             self.navigationController?.popViewControllerAnimated(true)
            
             showToast(NSLocalizedString("DELETE_PLACE_OK_TITLE", comment: ""), message: NSLocalizedString("DELETE_PLACE_OK_MSG", comment: ""), vc: self)
            
        }))
        
        presentViewController(alert, animated: true, completion: nil)
        
    }
    
    @IBAction func sharePressed(sender: UIButton) {
        shareContainer.hidden = false
    }
    
    @IBAction func facebookPressed(sender: UIButton) {
        
        if SLComposeViewController.isAvailableForServiceType(SLServiceTypeFacebook) {
            let fbShare:SLComposeViewController = SLComposeViewController(forServiceType: SLServiceTypeFacebook)
            fbShare.setInitialText(defaultMessage)
            self.presentViewController(fbShare, animated: true, completion: nil)
            
        } else {
            let alert = UIAlertController(title: NSLocalizedString("FB_LOGIN_TITLE", comment: ""), message: NSLocalizedString("FB_LOGIN_MSG", comment: ""), preferredStyle: UIAlertControllerStyle.Alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Default, handler: nil))
            self.presentViewController(alert, animated: true, completion: nil)
        }
    }
    
    @IBAction func twitterPressed(sender: UIButton) {
        if SLComposeViewController.isAvailableForServiceType(SLServiceTypeTwitter) {
            
            let tweetShare:SLComposeViewController = SLComposeViewController(forServiceType: SLServiceTypeTwitter)
            tweetShare.setInitialText(defaultMessage)
            self.presentViewController(tweetShare, animated: true, completion: nil)
            
        } else {
            
            let alert = UIAlertController(title: NSLocalizedString("TW_LOGIN_TITLE", comment: ""), message: NSLocalizedString("TW_LOGIN_MSG", comment: ""), preferredStyle: UIAlertControllerStyle.Alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Default, handler: nil))
            
            self.presentViewController(alert, animated: true, completion: nil)
        }
    }
    
    @IBAction func shareClosePressed(sender: UIButton) {
        shareContainer.hidden = true
    }
    
    @IBAction func navigatePressed(sender: UIButton) {
        //GoogleMaps
        if (UIApplication.sharedApplication().canOpenURL(NSURL(string:"comgooglemaps://")!)) {
            UIApplication.sharedApplication().openURL(NSURL(string:
                "comgooglemaps://?center=\(currentPlace.latitude),\(currentPlace.longitude)&zoom=14&views=traffic")!)
        } else {
        //Apple Maps
        let coordinate = CLLocationCoordinate2DMake(CLLocationDegrees(currentPlace.latitude)!, CLLocationDegrees(currentPlace.longitude)!)
        let mapItem = MKMapItem(placemark: MKPlacemark(coordinate: coordinate, addressDictionary:nil))
        mapItem.name = NSLocalizedString("MAPS_DESTINATION", comment: "")
        mapItem.openInMapsWithLaunchOptions([MKLaunchOptionsDirectionsModeKey : MKLaunchOptionsDirectionsModeDriving])
            
        if (UIApplication.sharedApplication().canOpenURL(NSURL(string:"http://maps.apple.com")!)) {
            UIApplication.sharedApplication().openURL(NSURL(string:
            "http://maps.apple.com/?ll=\(currentPlace.latitude),\(currentPlace.longitude)")!)
        } else {
            showToast(NSLocalizedString("ERR_MAPS_TITLE", comment: ""), message: NSLocalizedString("ERR_MAPS_MSG", comment: ""), vc: self)
            }
        }
    }

}
