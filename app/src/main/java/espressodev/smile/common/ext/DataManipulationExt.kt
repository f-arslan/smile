package espressodev.smile.common.ext

import espressodev.smile.data.service.model.Contact

fun List<Contact>.groupByLetter(): Map<Char, List<Contact>> = groupBy { it.firstName.first() }