package com.cmdv.data.repository

import androidx.lifecycle.MutableLiveData
import com.cmdv.data.entity.CategoryFirebaseEntity
import com.cmdv.data.mapper.CategoryFirebaseMapper
import com.cmdv.domain.model.CategoryModel
import com.cmdv.domain.repository.CategoryRepository
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

private const val DB_CATEGORIES_PATH = "categories"
private const val DB_CATEGORY_LANG_PATH_PLACEHOLDER = "categories_"

class CategoryRepositoryImpl : CategoryRepository {

	private val databaseRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

	private val categoriesDatabaseRef: DatabaseReference = databaseRootRef.getReference(DB_CATEGORIES_PATH)

	override fun fetchCategories(): MutableLiveData<List<CategoryModel>> {
		val categoriesMutableLiveData = MutableLiveData<List<CategoryModel>>()

		val categoriesByLanguageRef: DatabaseReference = categoriesDatabaseRef.child(getCategoryByLanguagePath())

		categoriesByLanguageRef.addValueEventListener(object : ValueEventListener {
			override fun onCancelled(p0: DatabaseError) {
				TODO("Not yet implemented")
			}

			override fun onDataChange(dataSnapshot: DataSnapshot) {
				val categories = ArrayList<CategoryModel>()
				for (ds in dataSnapshot.children) {
					val categoryFirebase: CategoryFirebaseEntity? = ds.getValue(CategoryFirebaseEntity::class.java)
					if (categoryFirebase != null) {
						categories.add(CategoryFirebaseMapper().transformEntityToModel(categoryFirebase))
					}
				}
				categoriesMutableLiveData.postValue(categories)
			}
		})

		return categoriesMutableLiveData
	}

	private fun getCategoryByLanguagePath(): String =
		DB_CATEGORY_LANG_PATH_PLACEHOLDER + getLang()

	private fun getLang(): String {
		val displayLanguage: String = Locale.getDefault().displayLanguage
		return if (displayLanguage != "es" && displayLanguage != "en") {
			"en"
		} else {
			displayLanguage
		}
	}

}