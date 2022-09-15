//
//  TableController.swift
//  Contacts
//
//  Created by Tsotne Babunashvili on 07.01.22.
//

import UIKit

class TableController : UIViewController {
 
    @IBOutlet var tableView:UITableView!
    private var onlyFavs : Bool = false
    private var image : String = "star"
    private var allContactsDict : [Character: [Contact]] = ["B": [Contact(firstName: "Tsotne", lastName: "Babunashvili", number: "555685305", id: 1323123)]]
    private var allIdsMap : [Int : Int] = [1323123 : 0]
    private var favIdsMap : [Int : Int] = [:]
    private var favContactsDict : [Character: [Contact]] = [:]
    let rowHeight = 40
    let headerHeight = 30
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        
        tableView.register(UINib(nibName: "TableViewCell", bundle: nil), forCellReuseIdentifier:"TableViewCell")
        let addButton = UIBarButtonItem(barButtonSystemItem: .add, target: self, action: #selector(addButtonTapped))
        let favoritesButton = UIBarButtonItem(image: UIImage.init(systemName: image), style: .plain, target: self, action: #selector(favouritesButtonTapped))
        navigationItem.rightBarButtonItem = addButton
        navigationItem.leftBarButtonItem = favoritesButton
    }

    
    
    @objc private func addButtonTapped(sender: UIButton) {
        let newContact = UIAlertController(title: "New Contact", message: "", preferredStyle: UIAlertController.Style.alert)

        newContact.addTextField { (firstName) in
            firstName.placeholder = "First Name"
        }
        newContact.addTextField { (lastName) in
            lastName.placeholder = "Last Name"
        }
        newContact.addTextField { (phoneNumber) in
            phoneNumber.placeholder = "Phone Number"
            phoneNumber.keyboardType = .phonePad
        }
        newContact.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.default, handler: { _ in
                    //Cancel Action
        }))
        newContact.addAction(UIAlertAction(title: "Save", style: UIAlertAction.Style.default, handler: {(_: UIAlertAction!) in
            let firstName : String? = newContact.textFields?[0].text
            let lastName : String? = newContact.textFields?[1].text
            let phoneNumber = newContact.textFields?[2].text
            let genId : Int = Int.random(in: 1..<10000000)
            let contact = Contact(firstName: firstName ?? "", lastName: lastName ?? "", number: phoneNumber ?? "", id: genId)
            if contact.firstName == "" && contact.lastName == "" {
                return
            }
            var keySec : Character
            if contact.lastName == "" {
                keySec = Array(contact.firstName.uppercased())[0]
            }   else {
                keySec = Array(contact.lastName.uppercased())[0]
            }
            if self.allContactsDict[keySec] == nil {
                var contacts : [Contact] = []
                contacts.append(contact)
                self.allContactsDict[keySec] = contacts
            }   else {
                var contacts = self.allContactsDict[keySec]
                contacts?.append(contact)
                contacts?.sort(by: {first, second in
                    let ffComp = first.lastName.uppercased()
                    let fsComp = first.firstName.uppercased()
                    let sfComp = second.lastName.uppercased()
                    let ssComp = second.firstName.uppercased()
                    if ffComp == sfComp {
                        return fsComp < ssComp
                    }   else {
                        return ffComp < sfComp
                    }
                })
                let size = contacts!.count-1
                for i in 0...size {
                    self.allIdsMap[contacts![i].id] = i
                }
                self.allContactsDict[keySec] = contacts
            }
            self.tableView.reloadData()
        }))
        
        self.present(newContact, animated: true, completion: nil)
    }

    
    @objc private func favouritesButtonTapped(sender: UIButton) {
        let image = onlyFavs ? "star" : "star.fill"
        let favoritesButton = UIBarButtonItem(image: UIImage.init(systemName: image), style: .plain, target: self, action: #selector(favouritesButtonTapped))
        navigationItem.leftBarButtonItem = favoritesButton
        onlyFavs = !onlyFavs
        tableView.reloadData()
    }
}

extension TableController:  UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // if !self.onlyFavs && contact.isFavourite
        let contactsDict = self.onlyFavs ? self.favContactsDict:self.allContactsDict
        let sortedKeyset = Array(contactsDict.keys).sorted()
        var contacts : [Contact]?
        contacts = contactsDict[sortedKeyset[section]]
        return contacts?.count ?? 0
    }

    func numberOfSections(in tableView: UITableView) -> Int {
        var count = 0
        if self.onlyFavs {
            count = self.favContactsDict.count
        }   else {
            count = self.allContactsDict.count
        }
        return count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let contactsDict = self.onlyFavs ? self.favContactsDict:self.allContactsDict
        let sortedKeyset = Array(contactsDict.keys).sorted()
        var contacts : [Contact]?
        contacts = contactsDict[sortedKeyset[indexPath.section]]
        let tableViewCell = tableView.dequeueReusableCell(withIdentifier: "TableViewCell", for: indexPath)
        if let cell = tableViewCell as? TableViewCell {
            cell.firstNameField.text = contacts?[indexPath.row].firstName
            cell.lastNameField.text = contacts?[indexPath.row].lastName
            let image = contacts?[indexPath.row].isFavourite ?? false ? "star.fill" : "star"
            cell.favouriteSelector.setImage(UIImage(systemName: image), for: .normal)
            cell.favouriteSelector.tag = indexPath.section * 98689 + indexPath.row;
            cell.favouriteSelector.addTarget(self, action: #selector(changeFavouriteStatus), for: .touchUpInside)
            return cell
        }
        return UITableViewCell()
    }

    @objc func changeFavouriteStatus(sender: UIButton) {
        let tag = sender.tag
        let sec = tag/98689
        let row = tag%98689
        let mapToSearch = self.onlyFavs ? self.favContactsDict : self.allContactsDict
        let sortedKeyset = Array(mapToSearch.keys).sorted()
        let contact = mapToSearch[sortedKeyset[sec]]![row]
   //     print(contact.firstName)
   //     print(contact.lastName)
        var image : String = "star"
        let findWith = contact.lastName != "" ? contact.lastName : contact.firstName
        let k = Array(findWith.uppercased())[0]
        if contact.isFavourite {
            contact.deleteFromFavourites()
            self.favContactsDict[k] = deleteFromDict(cont: contact, dict: self.favContactsDict, fromFavs : true)
        }   else {
            contact.addToFavourites()
            var onlyOne = false
            if self.favContactsDict[k] == nil {
                let contacts : [Contact] = []
                self.favContactsDict[k]=contacts
                self.favIdsMap[contact.id] = 0
                onlyOne = true
            }
            var cnt = self.favContactsDict[k]
            cnt?.append(contact)
            image = "star.fill"
            if !onlyOne {
                cnt?.sort(by: {first, second in
                    let ffComp = first.lastName.uppercased()
                    let fsComp = first.firstName.uppercased()
                    let sfComp = second.lastName.uppercased()
                    let ssComp = second.firstName.uppercased()
                    if ffComp == sfComp {
                        return fsComp < ssComp
                    }   else {
                        return ffComp < sfComp
                    }
                })
                let size = cnt!.count-1
                for i in 0...size {
                    self.favIdsMap[cnt![i].id] = i
                }
            }
            self.favContactsDict[k] = cnt
        }
        sender.setImage(UIImage(systemName: image), for: .normal)
        tableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let contactsDict = self.onlyFavs ? self.favContactsDict:self.allContactsDict
        let sortedKeySet = Array(contactsDict.keys).sorted()
        let keyChar = sortedKeySet[section]
        let headerView = UIView.init(frame: CGRect.init(x: 0, y: 0, width: Int(self.tableView.frame.width), height: headerHeight))
        let headerLabel = UILabel()
        headerLabel.frame = CGRect.init(x: 10, y: 10, width: headerView.frame.width - 15, height: headerView.frame.height - 15)
        headerLabel.text = "\(keyChar)"
        headerLabel.font = .boldSystemFont(ofSize: 17.0)
        headerLabel.textColor = .black
        headerView.backgroundColor = UIColor.lightGray
        headerView.addSubview(headerLabel)
        return headerView
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return CGFloat(self.rowHeight)
    }

    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return CGFloat(self.headerHeight)
    }

    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        
        if editingStyle == .delete {
           
            let sortedKeyset = Array(self.allContactsDict.keys).sorted()
            let contact = self.allContactsDict[sortedKeyset[indexPath.section]]![indexPath.row]
            print(contact.firstName)
            print(contact.lastName)
            if contact.isFavourite {
                self.favContactsDict[Array(contact.lastName.uppercased())[0]] = deleteFromDict(cont: contact, dict: self.favContactsDict,fromFavs: true)
             //   deleteFromFavs(cont: contact)
            }
            self.allContactsDict[Array(contact.lastName.uppercased())[0]] = deleteFromDict(cont: contact, dict: self.allContactsDict, fromFavs: false)
         /*   if self.allContactsDict["B"] == nil {
                print(3)
            }   else {
                print(self.allContactsDict["B"]?.count)
                
            } */
            tableView.reloadData()
   
        }
    }
    
    func deleteFromDict(cont: Contact, dict : [Character: [Contact]], fromFavs : Bool) -> [Contact]? {
        let findWith = cont.lastName != "" ? cont.lastName : cont.firstName
        
        let ind = fromFavs ? self.favIdsMap[cont.id] : self.allIdsMap[cont.id]
        var mapToChange = fromFavs ? self.favIdsMap : self.allIdsMap
            //dict[Array(cont.lastName.uppercased())[0]]?.firstIndex{$0 === cont} ?? 0
        var contacts = dict[Array(findWith.uppercased())[0]]
      //  print(contacts?[0].firstName)
      //  print(contacts?.count)
      //  print(ind)
        contacts?.remove(at: ind ?? 0)
     //   print(contacts?.count)
     /*   var s : Int = contacts?.count ?? 1
        for i in 0...s-1 {
            print(contacts?[i].lastName)
            print("MS")
        } */
      //   print(ind)
     //   print(contacts?.count)
        if contacts?.count == 0 {
            contacts = nil
        //    print(1)
        }   else {
            let s = contacts?.count ?? 1
            let size = s-1
            let start = ind ?? 0
            for i in start ... size {
             //   let curr = mapToChange[contacts?[i].id ?? 0]
             //   let nc = curr ?? 1
            //    let newCurr = nc - 1
                mapToChange[contacts?[i].id ?? 0]! = i
                   // newCurr
            }
        }
        if fromFavs {
            self.favIdsMap = mapToChange
        }   else {
            self.allIdsMap = mapToChange
        }
        return contacts //[Contact(firstName: "", lastName: "", number: "", mail: "")]
    }
}

class Contact {
    var firstName: String
    var lastName: String
    var number: String
    var isFavourite : Bool
    var id : Int
    init(firstName : String, lastName: String, number: String, id: Int) {
        self.firstName = firstName
        self.lastName = lastName
        self.number = number
        self.isFavourite = false
        self.id = id
    }
    
    func addToFavourites() {
        self.isFavourite = true
    }
    
    func deleteFromFavourites() {
        self.isFavourite = false
    }
}
