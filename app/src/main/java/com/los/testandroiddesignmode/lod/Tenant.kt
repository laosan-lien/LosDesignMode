package com.los.testandroiddesignmode.lod

import android.util.Log

private const val TAG = "Los:Tenant"

class Tenant {

    fun rentRoom(roomArea: Float, roomPrice: Float, mediator: Mediator) {
        Log.d(TAG, "rentRoom: 租到房 = ${mediator.rentOut(roomArea, roomPrice)}")
    }

}