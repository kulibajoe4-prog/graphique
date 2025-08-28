package org.babetech.borastock.data.mappers

import org.babetech.borastock.data.models.*
import org.babe.sqldelight.data.db.SelectAllStockItems
import org.babe.sqldelight.data.db.SelectAllStockEntries
import org.babe.sqldelight.data.db.SelectAllStockExits
import org.babe.sqldelight.data.db.SelectAllSuppliers

/**
 * Extension function to convert database supplier to domain model
 */
fun SelectAllSuppliers.toDomainModel(): Supplier {
    return Supplier(
        id = id,
        name = name,
        category = category,
        contactPerson = contact_person,
        email = email,
        phone = phone,
        address = address,
        city = city,
        country = country,
        rating = rating.toFloat(),
        status = SupplierStatus.valueOf(status),
        reliability = SupplierReliability.valueOf(reliability),
        lastOrderDate = last_order_date,
        paymentTerms = payment_terms,
        notes = notes,
        createdAt = created_at,
        updatedAt = updated_at
    )
}

/**
 * Extension function to convert database stock item to domain model
 */
fun SelectAllStockItems.toDomainModel(): StockItem {
    val supplier = Supplier(
        id = supplier_id,
        name = supplier_name,
        category = supplier_category,
        contactPerson = contact_person,
        email = supplier_email,
        phone = supplier_phone,
        address = null,
        city = null,
        country = null,
        rating = supplier_rating.toFloat(),
        status = SupplierStatus.valueOf(supplier_status),
        reliability = SupplierReliability.valueOf(supplier_reliability),
        lastOrderDate = null,
        paymentTerms = null,
        notes = null,
        createdAt = created_at,
        updatedAt = updated_at
    )

    return StockItem(
        id = id,
        name = name,
        category = category,
        description = description,
        currentStock = current_stock.toInt(),
        minStock = min_stock.toInt(),
        maxStock = max_stock.toInt(),
        unitPrice = unit_price,
        supplier = supplier,
        status = StockItemStatus.valueOf(status),
        createdAt = created_at,
        updatedAt = updated_at
    )
}

/**
 * Extension function to convert database stock entry to domain model
 */
fun SelectAllStockEntries.toDomainModel(): StockEntry {
    return StockEntry(
        id = id,
        stockItemId = stock_item_id,
        productName = product_name,
        category = product_category,
        quantity = quantity.toInt(),
        unitPrice = unit_price,
        totalValue = total_value,
        entryDate = entry_date,
        batchNumber = batch_number,
        expiryDate = expiry_date,
        supplier = supplier_name,
        supplierId = supplier_id,
        status = EntryStatus.valueOf(status),
        notes = notes,
        createdAt = created_at,
        updatedAt = updated_at
    )
}

/**
 * Extension function to convert database stock exit to domain model
 */
fun SelectAllStockExits.toDomainModel(): StockExit {
    return StockExit(
        id = id,
        stockItemId = stock_item_id,
        productName = product_name,
        category = product_category,
        quantity = quantity.toInt(),
        unitPrice = unit_price,
        totalValue = total_value,
        exitDate = exit_date,
        customer = customer,
        orderNumber = order_number,
        deliveryAddress = delivery_address,
        status = ExitStatus.valueOf(status),
        urgency = ExitUrgency.valueOf(urgency),
        notes = notes,
        createdAt = created_at,
        updatedAt = updated_at
    )
}