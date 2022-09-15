//
//  Contact+CoreDataProperties.swift
//  Collection Of Contacts
//
//  Created by Tsotne Babunashvili on 23.01.22.
//
//

import Foundation
import CoreData


extension Contact {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<Contact> {
        return NSFetchRequest<Contact>(entityName: "Contact")
    }

    @NSManaged public var firstName: String?
    @NSManaged public var lastName: String?
    @NSManaged public var phoneNumber: String?

}

extension Contact : Identifiable {

}
