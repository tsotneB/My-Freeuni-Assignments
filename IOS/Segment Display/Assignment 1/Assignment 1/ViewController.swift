//
//  ViewController.swift
//  Assignment 1
//
//  Created by Tsotne Babunashvili on 18.10.21.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet var firstDigit: [UIView]!
    @IBOutlet var secondDigit: [UIView]!
    @IBOutlet var thirdDigit: [UIView]!
    
    var digits = [0:[0,1,2,4,5,6],
                  1:[2,5],
                  2:[0,2,3,4,5],
                  3:[0,2,3,5,6],
                  4:[1,2,3,5],
                  5:[0,1,3,5,6],
                  6:[0,1,3,4,5,6],
                  7:[0,2,5],
                  8:[0,1,2,3,4,5,6],
                  9:[0,1,2,3,5,6]]
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        let num = 999;
        let first = num/100;
        let second = (num-first*100)/10;
        let third = num%10;
        for ind in digits[first]! {
            firstDigit[ind].backgroundColor = .red;
        }
        for ind in digits[second]! {
            secondDigit[ind].backgroundColor = .red;
        }
        for ind in digits[third]! {
            thirdDigit[ind].backgroundColor = .red;
        }
    }


}

