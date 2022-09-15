//
//  ViewController.swift
//  Collection Of Contacts
//
//  Created by Tsotne Babunashvili on 22.01.22.
//

import UIKit
import CoreData

class ViewController: UIViewController {

    @IBOutlet var collectionView : UICollectionView!
    var size = 1
    var colours = [UIColor.blue, UIColor.black, UIColor.red, UIColor.green, UIColor.yellow]
    var dbContext = DBManager.shared.persistentContainer.viewContext
    lazy var contacts = [Contact]()
    lazy var flowLayout: UICollectionViewFlowLayout = {
        let flowLayout = UICollectionViewFlowLayout()
        return flowLayout
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.collectionViewLayout = flowLayout
        
        collectionView.register(
            UINib(nibName: "ContactCell", bundle: nil),
            forCellWithReuseIdentifier: "ContactCell")
        
        collectionView.addGestureRecognizer(
            UILongPressGestureRecognizer(
                target: self, action: #selector(handleLongPress(gesture:))
            )
        )
        
        fetchContacts()
    }
    
    func fetchContacts() {
        let request = Contact.fetchRequest() as NSFetchRequest<Contact>
        
        do {
            contacts = try dbContext.fetch(request)
            collectionView.reloadData()
        } catch {}
    }
    
    @IBAction func add() {
        let alert = UIAlertController(title: "Add Contact", message: nil, preferredStyle:  .alert)
        
        var nameField : UITextField!
        alert.addTextField { textField in
            textField.placeholder = "Contact Name"
            nameField = textField
        }
        
        var numberField : UITextField!
        alert.addTextField { textField in
            textField.placeholder = "Contact Number"
            numberField = textField
        }
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        alert.addAction(UIAlertAction(title: "Save", style: .default, handler: { [unowned self] _ in
            guard let field = nameField, let name = field.text, !name.isEmpty else {return}
            guard let field2 = numberField, let num = field2.text, !num.isEmpty else {return}
            
            let contact = Contact(context: dbContext)
            contact.phoneNumber = num

            
            let ind = name.firstIndex(of: " ")
            if ind == nil {
                contact.firstName = name
            } else {
                let names = name.components(separatedBy:" ")
                contact.firstName = names[0]
                contact.lastName = names[1]
            }
            
            do {
                try dbContext.save()
                fetchContacts()
            } catch {}
        }))
        present(alert, animated: true, completion: nil)
    }
    
    @objc func handleLongPress(gesture: UILongPressGestureRecognizer) {
        let location = gesture.location(in: collectionView)
        if let indexPath = collectionView.indexPathForItem(at: location) {
            if let cell = collectionView.cellForItem(at: indexPath) {
                let cont = contacts[indexPath.row]
                let fName = cont.firstName ?? ""
                let lName = cont.lastName ?? ""
                let fullName = fName + " " + lName
                let alert = UIAlertController(title: "Delete Contact?", message: "Contact " + fullName + " will be deleted", preferredStyle: .alert)
                alert.addAction((UIAlertAction(title: "Delete", style: .destructive, handler: { [unowned self] _ in
                    dbContext.delete(cont)
                    do {
                        try dbContext.save()
                        fetchContacts()
                    } catch {}
                   // collectionView.deleteItems(at: [indexPath])
                })))
                alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
                present(alert, animated: true, completion: nil)
            }
        }
    }

}

extension ViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        contacts.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ContactCell", for: indexPath)
        if let contactCell = cell as? ContactCell {
            contactCell.phoneLabel.text = contacts[indexPath.row].phoneNumber
            contactCell.nameLabel.text = contacts[indexPath.row].firstName
            var inits = ""
            if contacts[indexPath.row].firstName != nil {
                inits.append(Array(contacts[indexPath.row].firstName!.uppercased())[0])
            }
            if contacts[indexPath.row].lastName != nil {
                inits.append(Array(contacts[indexPath.row].lastName!.uppercased())[0])
            }
            contactCell.initLabel.text = inits
       //     return contactCell
        }
        return cell
    }
    
}

extension ViewController: UICollectionViewDelegate {
    
}

extension ViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let spareWidth = collectionView.frame.width - (Constants.itemsInLine + 1) * Constants.spacing
        let size = spareWidth / Constants.itemsInLine
        return CGSize(width: size, height: size * 1.2 )
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsets(top: Constants.spacing, left: Constants.spacing, bottom: Constants.bottomSpacing, right: Constants.spacing)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return Constants.bottomSpacing
    }
}

extension ViewController {
    struct Constants {
        static let spacing: CGFloat = 15
        static let bottomSpacing: CGFloat = 30
        static let itemsInLine : CGFloat = 3
    }
}

