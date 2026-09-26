package com.kamalkavin96.tamilnadu_gov_api.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.kamalkavin96.tamilnadu_gov_api.exceptions.ResourceNotFoundException;
import com.kamalkavin96.tamilnadu_gov_api.models.State;
import com.kamalkavin96.tamilnadu_gov_api.repositories.StateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateService {

    private final StateRepository stateRepository;

    public List<State> findAll() {
        return stateRepository.findAll();
    }

    public State findById(Long id) {
        return stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State", "id", id));
    }

    public void create(Long sourceId, String stateName) {

        log.info("Starting state synchronization. sourceId={}, stateName={}", sourceId, stateName);

        final LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        Optional<State> existingState = stateRepository.findBySourceId(sourceId);

        State state;

        if (existingState.isPresent()) {
            state = existingState.get();
            log.info("State found. id={}, sourceId={}, name={}", state.getId(), state.getSourceId(), state.getName());
        } else {
            state = new State();
            state.setSourceId(sourceId);
            state.setName(stateName);
            state.setCreatedAt(now);
            log.info("State not found. Creating new state. sourceId={}, name={}", sourceId, stateName);
        }

        state.setSourceId(sourceId);
        state.setName(stateName);
        state.setUpdatedAt(now);
        State stateSaved = stateRepository.save(state);

        log.info("State saved successfully. id={}, sourceId={}, name={}", stateSaved.getId(), stateSaved.getSourceId(),
                stateSaved.getName());

    }

    @Transactional
    public State update(Long id, State entity) {
        State existing = findById(id);

        entity.setId(existing.getId());

        return stateRepository.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!stateRepository.existsById(id)) {
            throw new ResourceNotFoundException("State", "id", id);
        }

        stateRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return stateRepository.existsById(id);
    }
}
