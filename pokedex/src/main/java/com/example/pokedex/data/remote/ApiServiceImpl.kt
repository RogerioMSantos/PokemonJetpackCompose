package com.example.pokedex.data.remote

import com.example.pokedex.data.remote.dto.GetEvolutionChainResponse
import com.example.pokedex.data.remote.dto.GetPokemonListRequest
import com.example.pokedex.data.remote.dto.GetPokemonListResponse
import com.example.pokedex.data.remote.dto.GetPokemonResponse
import com.example.pokedex.data.remote.dto.GetPokemonSpeciesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(private val client: HttpClient): ApiService {
    override suspend fun getPokemons(offSet: GetPokemonListRequest): GetPokemonListResponse {
        try {
            return client.get {
                url(HttpRoutes.LIST_POKEMON)
                parameter("offset",offSet.offSet)
                parameter("limit",offSet.limit)
            }.body()
        }catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            return GetPokemonListResponse(0,"", mutableListOf())
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            return GetPokemonListResponse(0,"", mutableListOf())
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            return GetPokemonListResponse(0,"", mutableListOf())
        } catch(e: Exception) {
            println("Error: ${e.message}")
            return GetPokemonListResponse(0,"", mutableListOf())
        }
    }

    override suspend fun getPokemon(pokemon: Any): GetPokemonResponse {
        return try {
            client.get {
                url(HttpRoutes.POKEMON + "/$pokemon")
            }.body()
        }catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            return GetPokemonResponse(
                "",
                0,GetPokemonResponse.Sprites("",""),
                mutableListOf()
            )
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            return GetPokemonResponse(
                "",
                0,GetPokemonResponse.Sprites("",""),
                mutableListOf()
            )
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            return GetPokemonResponse(
                "",
                0,GetPokemonResponse.Sprites("",""),
                mutableListOf()
            )
        } catch(e: Exception) {
            println("Error: ${e.message}")
            return GetPokemonResponse(
                "",
                0,GetPokemonResponse.Sprites("",""),
                mutableListOf()
            )
        }

    }

    private suspend fun getPokemonSpeciesResponse(pokemon: Any):GetPokemonSpeciesResponse {
        return try {
            client.get {
                url(HttpRoutes.SPECIE + "/$pokemon")
            }.body()
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            return GetPokemonSpeciesResponse(GetPokemonSpeciesResponse.EvolutionChain(""))
        } catch (e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            return GetPokemonSpeciesResponse(GetPokemonSpeciesResponse.EvolutionChain(""))
        } catch (e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            return GetPokemonSpeciesResponse(GetPokemonSpeciesResponse.EvolutionChain(""))
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return GetPokemonSpeciesResponse(GetPokemonSpeciesResponse.EvolutionChain(""))
        }
    }

    override suspend fun getEvolutionChain(pokemon: Any): GetEvolutionChainResponse {
        val url = getPokemonSpeciesResponse(pokemon).evolutionChain.url

        return try {
            client.get {
                url(url)
            }.body()
        }catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            return GetEvolutionChainResponse(
                GetEvolutionChainResponse.Chain(
                    mutableListOf(),
                    GetEvolutionChainResponse.Species("",""))
            )
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            return GetEvolutionChainResponse(
                GetEvolutionChainResponse.Chain(
                    mutableListOf(),
                    GetEvolutionChainResponse.Species("","")))
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            return GetEvolutionChainResponse(
                GetEvolutionChainResponse.Chain(
                    mutableListOf(),
                    GetEvolutionChainResponse.Species("","")))
        } catch(e: Exception) {
            println("Error: ${e.message}")
            return GetEvolutionChainResponse(
                GetEvolutionChainResponse.Chain(
                    mutableListOf(),
                    GetEvolutionChainResponse.Species("","")))
        }
    }
}