//
//  Place.swift
//  Placeholder
//
//  Created by Andrea Montanari on 12/08/16.
//  Copyright © 2016 Andrea Montanari. All rights reserved.
//

import RealmSwift
import Foundation

// Dog model
class Place: Object {
    dynamic var streetName: String? // Properties can be optional
    var latitude: Double!
    var longitude: Double!
    dynamic var savedOn: NSDate!
    dynamic var placeComment: String?
    
}
