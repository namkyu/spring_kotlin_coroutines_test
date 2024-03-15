package com.example.co.application

import com.example.co.common.utils.ModelMapperUtils
import com.example.co.dto.request.AccountParam
import com.example.co.dto.response.AccountDTO
import com.example.co.infra.AccountRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AccountService(private val accountRepository: AccountRepository) {

    suspend fun getAllAccounts(): List<AccountDTO> {
        val accountList = accountRepository.findAll().toList() // 비동기적으로 생성된 값을 리스트로 변환
        return ModelMapperUtils.mapToList(accountList)
    }

    suspend fun saveAccount(accountParam: AccountParam) {
        accountRepository.save(accountParam.toEntity())
    }

    @Transactional
    suspend fun updateAccount(accountParam: AccountParam, accountId: Long) {
        val accountEntity = accountRepository.findById(accountId)
        accountEntity?.let {
            it.name = accountParam.name
            it.email = accountParam.email
            accountRepository.save(it)
        }
    }
}