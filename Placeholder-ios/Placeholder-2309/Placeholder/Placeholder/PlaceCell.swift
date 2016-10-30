//
//  PlaceCell.swift
//  Placeholder
//
//  Created by Andrea Montanari on 19/08/16.
//  Copyright © 2016 Andrea Montanari. All rights reserved.
//

import UIKit

class PlaceCell: UITableViewCell {
    
    @IBOutlet weak var addressLbl: UILabel!
    @IBOutlet weak var coordsLbl: UILabel!
    @IBOutlet weak var commentLbl: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.layer.cornerRadius = 5.0
        self.clipsToBounds = true
    }
    /*
     override func setSelected(selected: Bool, animated: Bool) {
     super.setSelected(selected, animated: animated)
     }
     */
    
    func configureCell(address: String, coords: String, comment: String) {
        addressLbl.text = address
        coordsLbl.text = coords
        commentLbl.text = comment
    }
    
}
