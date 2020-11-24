package com.example.futworld

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class Player{
    var id: String? = null
        get() = field
        set(value) {
            field = value
        }
    var name: String? = null
        get() = field
        set(value) {
            field = value
        }
    var position: String? = null
        get() = field
        set(value) {
            field = value
        }
    var number: String? = null
        get() = field
        set(value) {
            field = value
        }
    var age: String? = null
        get() = field
        set(value) {
            field = value
        }
    var photo: String? = null
        get() = field
        set(value) {
            field = value
        }
    var team: String? = null
        get() = field
        set(value) {
            field = value
        }
    var favTeam: String? = null
        get() = field
        set(value) {
            field = value
        }

    constructor(){}

    constructor(id: String,
                name: String,
                position: String,
                number: String,
                age: String,
                photo: String,
                team: String,
                favTeam: String){
        this.id = id
        this.name = name
        this.position = position
        this.number = number
        this.age = age
        this.photo = photo
        this.team = team
        this.favTeam = favTeam
    }
}