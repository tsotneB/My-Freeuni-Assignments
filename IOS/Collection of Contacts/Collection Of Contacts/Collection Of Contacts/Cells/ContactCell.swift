//
//  ContactCell.swift
//  Collection Of Contacts
//
//  Created by Tsotne Babunashvili on 23.01.22.
//

import UIKit

class ContactCell: UICollectionViewCell {

    @IBOutlet var backview : UIView!
    @IBOutlet var initView : UIView!
    @IBOutlet var phoneLabel : UILabel!
    @IBOutlet var nameLabel : UILabel!
    @IBOutlet var initLabel : UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    
    override func layoutSubviews() {
        super.layoutSubviews()
    
        nameLabel.textColor = .gray
        backview.layer.cornerRadius = 10 //backview.frame.width / 20
        backview.clipsToBounds = true
        backview.layer.borderColor = UIColor.black.cgColor
        backview.layer.borderWidth = 1
        backview.layoutIfNeeded()
      
        initView.layer.cornerRadius = initView.frame.width / 2
        initView.clipsToBounds = true
     }

}
