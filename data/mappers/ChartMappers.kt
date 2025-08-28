package org.babetech.borastock.data.mappers

import org.babetech.borastock.data.models.*
import org.babe.sqldelight.data.db.SelectStockStatistics
import org.babe.sqldelight.data.db.SelectRecentEntries
import org.babe.sqldelight.data.db.SelectRecentExits

/**
 * Extension function to convert database statistics to domain model
 */
fun SelectStockStatistics.toDomainModel(): StockStatistics {
    return StockStatistics(
        totalItems = total_items.toInt(),
        itemsInStock = items_in_stock.toInt(),
        itemsLowStock = items_low_stock.toInt(),
        itemsOutOfStock = items_out_of_stock.toInt(),
        itemsOverstocked = items_overstocked.toInt(),
        totalStockValue = total_stock_value ?: 0.0
    )
}

/**
 * Extension function to convert recent entries to recent movements
 */
fun SelectRecentEntries.toRecentMovement(): RecentMovement {
    return RecentMovement(
        id = id,
        productName = product_name,
        quantity = quantity.toInt(),
        date = entry_date,
        type = MovementType.ENTRY,
        description = "Entrée: ${quantity} x ${product_name} de ${supplier_name}"
    )
}

/**
 * Extension function to convert recent exits to recent movements
 */
fun SelectRecentExits.toRecentMovement(): RecentMovement {
    return RecentMovement(
        id = id,
        productName = product_name,
        quantity = quantity.toInt(),
        date = exit_date,
        type = MovementType.EXIT,
        description = "Sortie: ${quantity} x ${product_name} pour ${customer}"
    )
}