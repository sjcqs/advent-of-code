package puzzle02

data class Entry(val policy: PasswordPolicy, val password: String) {
    val isValidAccordingToCount = policy.isValidAccordingToCount(password)
    val isValidAccordingToIndices = policy.isValidAccordingToIndices(password)
}