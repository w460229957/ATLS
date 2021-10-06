//
//  ConcentrationTest.swift
//  Project
//
//  Created by 张正源 on 10/5/21.
//

import UIKit
import Foundation

class ConcentrationTest: NSObject {
    
    static var normalIntList:[Int] = []
    
    static func generateRandomNumber(){
        for _ in 0...19{
            normalIntList.append(Int.random(in: 10000..<99999))
        }
    }
    static func generateRandomNumber(_ howmany:Int)->[Int]{
        var normalIntList:[Int] = []
        for _ in 0...howmany-1{
            normalIntList.append(Int.random(in: 10000..<99999))
        }
        return normalIntList
    }
    static func revertNumber()->[String]{
        var reversedNumber:[String] = []
        for each in normalIntList{
            reversedNumber.append(String(String(each).reversed()))
        }
        return reversedNumber
    }
    
    static func revertNumber(_ input:[Int])->[String]{
        var reversedNumber:[String] = []
        for each in input{
            reversedNumber.append(String(String(each).reversed()))
        }
        return reversedNumber
    }
    
}
