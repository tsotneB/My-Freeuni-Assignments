//
//  TableViewCell.swift
//  Contacts
//
//  Created by Tsotne Babunashvili on 07.01.22.
//

import UIKit

class TableViewCell: UITableViewCell {

    @IBOutlet var firstNameField:UILabel!
    @IBOutlet var lastNameField:UILabel!
    @IBOutlet var favouriteSelector:UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
