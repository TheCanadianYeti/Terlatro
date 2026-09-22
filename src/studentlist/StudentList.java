/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package studentlist;

/**
 *
 * @author tatto
 */
public class StudentList {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Student s1 = new Student(101, "Griffin");
        Student s2 = new Student(102, "Jacop");
        Student s3 = new Student(103, "Marcus");
        Student studentList [] = new Student[3]; 
        studentList[0] = s1;
        studentList[1] = s2;
        studentList[2] = s3;
        
        for(Student s: studentList){
            System.out.print("student 1's name is: " + s.getName());
            System.out.print(" and their id is: " + s.getID());
            System.out.println("");
        }
        
    }
    
}
