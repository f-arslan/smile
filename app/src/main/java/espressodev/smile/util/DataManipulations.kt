package espressodev.smile.util

import espressodev.smile.model.room.ContactEntity

fun turnListToGroupByLetter(data: List<ContactEntity>): List<List<ContactEntity>> {
    return data.groupBy { it.firstName.first().toString() }.values.toList()
}