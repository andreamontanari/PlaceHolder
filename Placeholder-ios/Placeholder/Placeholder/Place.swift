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
    dynamic var streetName: String!
    dynamic var latitude: String!
    dynamic var longitude: String!
    dynamic var savedOn: NSDate!
    dynamic var placeComment: String?
    
    var latlng: String {
        get {
            return "\(latitude), \(longitude)"
        }
    }
    
}
