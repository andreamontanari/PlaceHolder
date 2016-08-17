//
//  PlacesViewController.swift
//  Placeholder
//
//  Created by Andrea Montanari on 11/08/16.
//  Copyright © 2016 Andrea Montanari. All rights reserved.
//

import Foundation
import UIKit


class PlacesViewController: UIViewController {
    
    @IBOutlet weak var backBtn: UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    @IBAction func goBack(sender: UIBarButtonItem) {
        dismissViewControllerAnimated(true, completion: nil)
    }
    
}
