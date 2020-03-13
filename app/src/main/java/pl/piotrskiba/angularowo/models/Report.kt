package pl.piotrskiba.angularowo.models

import java.io.Serializable

class Report(val id: Int, val status: String, val appreciation: String, val date: String, val reporter: String, val reported: String, val reason: String, val isArchived: Boolean) : Serializable