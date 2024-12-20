package com.example.workoutapp.data.hiltmodules
import com.example.workoutapp.data.diet.api.CalorieNinjaApi
import com.example.workoutapp.data.diet.model.DietRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DietModule {

    @Provides
    @Singleton
    fun provideCalorieNinjaApi(): CalorieNinjaApi {
        return Retrofit.Builder()
            .baseUrl("https://api.calorieninjas.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CalorieNinjaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDietRepository(api: CalorieNinjaApi): DietRepository {
        return DietRepository(api)
    }

}