enum class Grade(val value: Int) {
    A(5),
    B(4),
    C(3),
    NOT_FINISHED(0)
}

data class Course(
    val title: String,
    val grade: Grade,
)

data class Student(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val emails: List<String>,
    val phones: List<String>,
    val courses: List<Course>
)

fun main() {
    val students = loadStudents()
    val ivans = students.filter { it.firstName == "Ivan" }
    val adultNotIvans = students.filter { it.firstName != "Ivan" && it.age >= 18 }

    val studentWithContacts = students.filter { it.emails.isNotEmpty() || it.phones.isNotEmpty() }
    val fullNames = students.map { "${it.firstName} ${it.lastName} - ${it.age}" }

    val studentAndTheirAvgGrade = students.map { student ->
        val avg = student.courses
            .filter { course -> course.grade != Grade.NOT_FINISHED }
            .map { course -> course.grade.value }
            .average()
        student to avg
    }
}


fun loadStudents(): List<Student> {
    return emptyList()
}