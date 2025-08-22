package com.nompay.banking_universal.repositories.enums.user

enum class UserRoles(val role: String, roleId: Int) {
  ADMIN("Admin", 1),
  USER("User", 2),
  FINANCIER("Financier", 3),
  ONLOOKER("Onlooker", 4);

  companion object {
    fun isSuchRole(roleName: String): String {
      return entries.find { it.role.equals(roleName, ignoreCase = true) }?.role ?: "Not present"
    }
  }
}