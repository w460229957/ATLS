//
//  ShortTest.swift
//  Project
//
//  Created by 张正源 on 10/5/21.
//

import UIKit
import Foundation

class ShortTest: NSObject{
    static var result:[UIImage] = []
    static var currentIndes:Int = 0
    
    static func generateRandomImages(){
        result = []
        for _ in 0...19{
            result.append(UIImage(named: "Image-"+String(Int.random(in:0..<50)))!)
        }
    }
    static func generateRandomImages(_ number:Int)->[UIImage]{
        var result:[UIImage] = []
        for _ in 0..<number{
            result.append(UIImage(named: "Image-"+String(Int.random(in:0..<50)))!)
        }
        return result
    }
    static func returnSigletonImage()->[UIImage]{
        return result
    }
    
}
