package espressodev.smile.common.ext

import espressodev.smile.data.service.model.Contact

fun List<Contact>.groupByLetter(): List<Pair<String, List<Contact>>> {
    return this.groupBy { it.firstName.first().uppercase() }.toList().sortedBy { it.first }
}