//
//  PlacesViewController.swift
//  Placeholder
//
//  Created by Andrea Montanari on 11/08/16.
//  Copyright © 2016 Andrea Montanari. All rights reserved.
//

import Foundation
import UIKit
import RealmSwift

class PlacesViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UIGestureRecognizerDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var backBtn: UIBarButtonItem!
    
    var places: Results<Place>?
    
    var currentPlace: Place!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        let longPressGesture:UILongPressGestureRecognizer = UILongPressGestureRecognizer(target: self, action: #selector(PlacesViewController.handleLongPress(_:)))
        
        longPressGesture.minimumPressDuration = 1.0 // 1 second press
        longPressGesture.delegate = self
        self.tableView.addGestureRecognizer(longPressGesture)
        
    }

    func handleLongPress(longPressGesture:UILongPressGestureRecognizer) {
        
        let p = longPressGesture.locationInView(self.tableView)
        let indexPath = self.tableView.indexPathForRowAtPoint(p)
        
        if (indexPath != nil) && (longPressGesture.state == UIGestureRecognizerState.Began) {
            let copiedPlace = places![indexPath!.row]
            UIPasteboard.generalPasteboard().string = "\(copiedPlace.streetName), (\(copiedPlace.latlng))"
            showToast("", message: NSLocalizedString("PLACE_COPIED", comment: ""), vc: self)
        }
        
    }
    
    override func viewWillAppear(animated: Bool) {
        // Do any additional setup after loading the view, typically from a nib.
        // Get the default Realm
        // You only need to do this once (per thread)
        let realm = try! Realm()
    
        places = realm.objects(Place.self).sorted("savedOn", ascending: false)
        
        tableView.reloadData()
    }

    @IBAction func goBack(sender: UIBarButtonItem) {
        self.navigationController?.popViewControllerAnimated(true)
        //dismissViewControllerAnimated(true, completion: nil)
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        if let cell = tableView.dequeueReusableCellWithIdentifier("placeCell") as? PlaceCell {
            
            if let place = places![indexPath.row] as? Place {
            
                let placeholder = NSLocalizedString("PLACE_COMMENT_PLACEHOLDER", comment: "")
                
                let comment = place.placeComment == nil || place.placeComment == "" ? "\"\(placeholder)\"" : "\"\(place.placeComment!)\""
                cell.configureCell(place.streetName, coords: place.latlng, comment: comment)
                return cell
            } else {
                return PlaceCell()
            }
        } else {
            return PlaceCell()
        }
        
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return places!.count
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        currentPlace = places![indexPath.row]
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "placeIdentifier" {
            //Note that, originally, destinationViewController is of Type UIViewController and has to be casted as myViewController instead since that's the ViewController we trying to go to.
            let destinationVC = segue.destinationViewController as! DetailViewController
            
            if let indexPath = self.tableView.indexPathForSelectedRow {
                
                let selectedPlace = places![indexPath.row]
                destinationVC.currentPlace = selectedPlace
            }
        }
    }
    
}
