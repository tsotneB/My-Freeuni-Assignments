//
//  ViewController.swift
//  Assignment 2
//
//  Created by Tsotne Babunashvili on 18.11.21.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet var statusView: UIView!
    @IBOutlet var profilPicture: UIImageView!
    @IBOutlet var facebook: UIButton!
    @IBOutlet var instagram: UIButton!
    @IBOutlet var twitter: UIButton!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        profilPicture.layer.cornerRadius = profilPicture.frame.width/2
        profilPicture.clipsToBounds = true
        
        statusView.layer.cornerRadius = statusView.frame.width/8
        statusView.clipsToBounds = true
    
        facebook.layer.cornerRadius = facebook.frame.width/3
        facebook.clipsToBounds = true
        
        instagram.layer.cornerRadius = instagram.frame.width/3
        instagram.clipsToBounds = true
        
        twitter.layer.cornerRadius = twitter.frame.width*3/5
        twitter.clipsToBounds = true
    }


}

