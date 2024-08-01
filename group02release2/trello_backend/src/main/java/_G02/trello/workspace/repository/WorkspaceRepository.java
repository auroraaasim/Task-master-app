package _G02.trello.workspace.repository;


import _G02.trello.workspace.model.WorkspaceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceModel, Long> {
    List<WorkspaceModel> findAllByUsers_Id(int users_id);
}