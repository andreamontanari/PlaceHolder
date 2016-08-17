//
//  MapsViewController.swift
//  Placeholder
//
//  Created by Andrea Montanari on 11/08/16.
//  Copyright © 2016 Andrea Montanari. All rights reserved.
//

import Foundation
import UIKit
import GoogleMaps
import RealmSwift

class MapsViewController: UIViewController, CLLocationManagerDelegate, GMSMapViewDelegate {
    
    @IBOutlet weak var mapView: GMSMapView!
    @IBOutlet weak var doneBtn: UIBarButtonItem!
    
    @IBOutlet weak var pinBtn: UIButton!
    @IBOutlet weak var msgBtn: UIButton!
    
    let locationManager = CLLocationManager()

    var latitude: CLLocationDegrees = 0.0
    var longitude: CLLocationDegrees = 0.0
    
    var current_streetName: String?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mapView.delegate = self
        locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
    
        self.mapView.bringSubviewToFront(self.pinBtn)
        self.mapView.bringSubviewToFront(self.msgBtn)
    }
    
    func locationManager(manager: CLLocationManager, didChangeAuthorizationStatus status: CLAuthorizationStatus) {
        // 3
        if status == .AuthorizedWhenInUse {
            
            // 4
            locationManager.startUpdatingLocation()
            
            //5
            mapView.myLocationEnabled = true
        }
    }
    
    // 6
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.first {
            
            // 7
            mapView.camera = GMSCameraPosition(target: location.coordinate, zoom: 15, bearing: 0, viewingAngle: 0)
            
            // 8
            locationManager.stopUpdatingLocation()
        }
        
    }
    
    func mapView(mapView: GMSMapView, idleAtCameraPosition position: GMSCameraPosition) {
        reverseGeocodeCoordinate(position.target)
    }
    
    func reverseGeocodeCoordinate(coordinate: CLLocationCoordinate2D) {
        
        // 1
        let geocoder = GMSGeocoder()
        
        // 2
        geocoder.reverseGeocodeCoordinate(coordinate) { response, error in
            if let address = response?.firstResult() {
                
                // 3
                let lines = address.lines! as [String]
                //self.addressLabel.text = lines.joinWithSeparator("\n")
                //print(lines.joinWithSeparator(" "))
                self.current_streetName = lines.joinWithSeparator(" ")
                // 4
                UIView.animateWithDuration(0.25) {
                    self.view.layoutIfNeeded()
                }
            }
        }
    }

    // function to store the user current location
    @IBAction func storePlace(sender: UIButton) {
        
        let coords = mapView.myLocation?.coordinate
        
        latitude = coords!.latitude
        longitude = coords!.longitude
        
        let streetName = self.current_streetName == nil ? "No address found" : self.current_streetName
    
        let marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        marker.icon = UIImage(named: "pin_added")
        marker.title = "Current P8lace"
        marker.snippet = streetName
        marker.map = mapView
        
        let place = Place()
        place.latitude = latitude
        place.longitude = longitude
        place.savedOn = NSDate()
        place.streetName = streetName
        place.placeComment = ""
        
        print(place.latitude)
        print(place.longitude)
        print(place)
        
        // Get the default Realm
        let realm = try! Realm()
        // You only need to do this once (per thread)
        
        // Add to the Realm inside a transaction
        try! realm.write {
            realm.add(place)
        }
        
        let dialog = UIAlertController(title: "Place saved",
                                       message: "Your GPS coordinates have been saved correctly!",
                                       preferredStyle: UIAlertControllerStyle.Alert)
        // Present the dialog
        presentViewController(dialog,
                              animated: false,
                              completion: {
                                let delayTime = dispatch_time(DISPATCH_TIME_NOW, Int64(1 * Double(NSEC_PER_SEC)))
                                dispatch_after(delayTime, dispatch_get_main_queue()) {
                                    dialog.dismissViewControllerAnimated(true, completion: nil)
                                }
                                
                                                })
        
        msgBtn.hidden = false;
        
        
    }
    
    // function to store the message to the related saved place
    @IBAction func writeMessage(sender: UIButton) {
        
        //open dialog
        let alert = UIAlertController(title: "Write a comment", message: "Post an additional note for the saved place", preferredStyle: UIAlertControllerStyle.Alert)
        
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertActionStyle.Cancel, handler:
            { (action: UIAlertAction!) in
                alert.dismissViewControllerAnimated(true, completion: nil)
        }))
        
        // configure text field
        alert.addTextFieldWithConfigurationHandler({(textField: UITextField) in
            textField.textColor = UIColor.blackColor()
            textField.placeholder = "Your note"
            })

        alert.addAction(UIAlertAction(title: "Save", style: UIAlertActionStyle.Default, handler:{ (UIAlertAction)in
            //read it
            let textField = (alert.textFields?.first)! as UITextField
            //save it
            // Get the default Realm
            // You only need to do this once (per thread)
            let realm = try! Realm()
            
            let predicate = NSPredicate(format: "latitude = %@ AND longitude = %@", self.latitude, self.longitude)

            // Add to the Realm inside a transaction
            try! realm.write {
                realm.objects(Place.self).filter(predicate).first?.placeComment = textField.text
            }
            
            self.msgBtn.hidden = true
            self.mapView.clear()
        }))
        
        presentViewController(alert, animated: true, completion: nil)
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func goBack(sender: UIBarButtonItem) {
        dismissViewControllerAnimated(true, completion: nil)
    }
    
}
