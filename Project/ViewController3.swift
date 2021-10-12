//
//  ViewController3.swift
//  Project
//
//  Created by 张正源 on 10/5/21.
//

import UIKit

class ViewController3: UIViewController,UITextFieldDelegate {
    var totalScore = 0
    var timer:Timer?
    @IBOutlet weak var button: UIButton!
    @IBOutlet weak var textTitle: UIStackView!
    @IBOutlet weak var answerfield: UITextField!
    
    @IBOutlet weak var imageArea: UIImageView!
    @IBOutlet weak var numberField: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        answerfield.delegate = self
        // Do any additional setup after loading the view.
        
        ////This part of code is from the textbook////////////////////////////////////////////////
        NotificationCenter.default.addObserver(self, selector:#selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        
        NotificationCenter.default.addObserver(self, selector:#selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
         view.addGestureRecognizer(tap)
        
    }

    @objc func dismissKeyboard() {
          view.endEditing(true)
       }
      
      @objc func keyboardWillShow(notification: NSNotification) {
       if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
       if self.view.frame.origin.y == 0 {
                                          self.view.frame.origin.y -= keyboardSize.height-150
                                              }//I added -150 to adjust the y coordinate of the frame
                                  }
                  }
      
      @objc func keyboardWillHide(notification: NSNotification) {
       if ((notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue) != nil {
                  if self.view.frame.origin.y != 0 {
                                      self.view.frame.origin.y = 0
                      }
              }
       }
/////////////////////////////////////////////////////////////////////////////////////
    func removeTextTitle(){
        textTitle.isHidden = true
    }
    
    func showTextTitle(){
        textTitle.isHidden = false
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func setNumberField(_ input:Int){
        numberField.text = "image: "+String(input)
    }
    
    func showTextField(){
        answerfield.isHidden = false
    }
    
    func hideTextField(){
        answerfield.isHidden = true
    }
    
    func getTextField()-> String{
        return answerfield.text!
    }
    func buttonStart(){
        button.setTitle("Start", for: .normal)
    }
    
    func buttonStop(){
        button.setTitle("Stop", for: .normal)
    }
    
    func CheckIfRight(CorrectAnswer str1:String,UserAnswer str2:String)->Bool{
        numberField.isHidden = false
        numberField.text = "CorrectAnswer: \(str1) Your Answer: \(str2)"
        if(str1 == str2){
            return true
        }
        else{
            
            return false
        }
    }
    
    func IncrementScore(){
        totalScore += 1
    }
    func CalculateResultANDShow(_ result:Int){
        timer?.invalidate()
        showTextTitle()
        hideTextField()
        answerfield.text! = ""
        numberField.text = "Your final score is \(round((Double(result)/30)*100))"
        if(imageArea.isAnimating){
            imageArea.stopAnimating()
        }
        imageArea.image = nil
        imageArea.isHidden = true
        
    }
    func showArea(){
        imageArea.isHidden = false
    }
    
    func hideArea(){
        imageArea.isHidden = true
    }
    
    func startTest(){
        removeTextTitle()
        numberField.text = "wait..."
        var testset = ShortTest.generateRandomImages(30)
        var answerSet:[UIImage] = []
        var correctID = 0
        var time = 0
        var flag:Bool = true
        totalScore = 0
        timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true, block: {
            timer in
            time -= 1
            if(time <= 0){
                        if(flag == false){
                            self.imageArea.stopAnimating()
                            correctID = Int.random(in: 1...10)
                            for each in answerSet{
                                if(each.imageAsset == answerSet[correctID-1].imageAsset){
                                    correctID = answerSet.firstIndex(of: each)! + 1
                                    break
                                }
                            }
                            self.numberField.text = "What is the order of this picture in the sequence?(First Occurrence)"
                            self.imageArea.image = answerSet[correctID-1]
                            flag = true
                            time = 10
                            self.showTextField()
                        }
                        else{
                            self.numberField.text = ""
                            flag = false
                            time = 16
                            if(testset.count == 0){
                                self.CalculateResultANDShow(self.totalScore)
                            }
                            else{
                                if(self.CheckIfRight(CorrectAnswer: String(correctID), UserAnswer: self.getTextField())){
                                        self.IncrementScore()
                                    }
                                    answerSet = []
                                    for _ in 1...10{
                                        answerSet.append(testset.last!)
                                        testset = testset.dropLast()
                                    }
                                    self.imageArea.animationImages = answerSet
                                    self.imageArea.animationDuration = 16.0
                                    self.imageArea.animationRepeatCount = 1
                                    self.imageArea.startAnimating()
                                    self.showArea()
                                    self.hideTextField()
                                    self.answerfield.text = ""
                                    
                            }

                    }
                }

            }
        )
        
    }
    
    @IBAction func StartOr(_ sender: UIButton) {
        if(sender.currentTitle == "Start!"){
            sender.setTitle("Stop!", for: .normal)
            self.startTest()
        }
        else if(sender.currentTitle == "Restart!"){
            sender.setTitle("Start!", for: .normal)
            numberField.text = ""
            numberField.isHidden = true
        }
        else{
            sender.setTitle("Restart!", for: .normal)
            self.CalculateResultANDShow(self.totalScore)
        }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
